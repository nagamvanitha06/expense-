package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppLanguage
import com.example.data.model.BudgetEntity
import com.example.data.model.ExpenseCategory
import com.example.data.model.ExpenseEntity
import com.example.data.model.FavoritePresetEntity
import com.example.data.model.ShoppingItemEntity
import com.example.data.model.TransactionType
import com.example.data.repository.ExpenseRepository
import com.example.util.ParsedVoiceExpense
import com.example.util.VoiceExpenseParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _currencySymbol = MutableStateFlow("₹")
    val currencySymbol: StateFlow<String> = _currencySymbol.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow("ALL") // ALL, EXPENSE, INCOME, TODAY, THIS_MONTH
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    // Current Month String "YYYY-MM"
    private val currentMonthYear: String
        get() {
            val cal = Calendar.getInstance()
            return String.format("%04d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1)
        }

    // All raw expenses
    val allExpenses: StateFlow<List<ExpenseEntity>> = repository.allExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Budgets
    val budgets: StateFlow<List<BudgetEntity>> = repository.getBudgetsForMonth(currentMonthYear)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Shopping list
    val shoppingItems: StateFlow<List<ShoppingItemEntity>> = repository.allShoppingItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Favorite Presets
    val favoritePresets: StateFlow<List<FavoritePresetEntity>> = repository.allFavoritePresets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered expenses based on search & filter tabs
    val filteredExpenses: StateFlow<List<ExpenseEntity>> = combine(
        allExpenses,
        _searchQuery,
        _selectedFilter
    ) { list, query, filter ->
        val now = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val startOfToday = cal.timeInMillis

        cal.set(Calendar.DAY_OF_MONTH, 1)
        val startOfMonth = cal.timeInMillis

        list.filter { item ->
            // Search text
            val matchesQuery = if (query.isBlank()) true else {
                item.title.contains(query, ignoreCase = true) ||
                        item.notes.contains(query, ignoreCase = true) ||
                        item.category.contains(query, ignoreCase = true) ||
                        item.amount.toString().contains(query)
            }

            // Filter selection
            val matchesFilter = when (filter) {
                "EXPENSE" -> item.type == "EXPENSE"
                "INCOME" -> item.type == "INCOME"
                "TODAY" -> item.timestamp >= startOfToday
                "THIS_MONTH" -> item.timestamp >= startOfMonth
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun setCurrency(symbol: String) {
        _currencySymbol.value = symbol
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedFilter(filter: String) {
        _selectedFilter.value = filter
    }

    // Add / Update / Delete Transactions
    fun addTransaction(
        title: String,
        amount: Double,
        type: TransactionType,
        category: ExpenseCategory,
        paymentMethod: String = "UPI / GPay",
        notes: String = "",
        timestamp: Long = System.currentTimeMillis()
    ) {
        viewModelScope.launch {
            repository.insertExpense(
                ExpenseEntity(
                    title = title.trim(),
                    amount = amount,
                    type = type.name,
                    category = category.id,
                    timestamp = timestamp,
                    paymentMethod = paymentMethod,
                    notes = notes.trim()
                )
            )
        }
    }

    fun deleteTransaction(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    fun toggleFavorite(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.updateExpense(expense.copy(isFavorite = !expense.isFavorite))
        }
    }

    // Set Budget
    fun setMonthlyBudget(limit: Double, category: String = "TOTAL") {
        viewModelScope.launch {
            repository.setBudget(
                BudgetEntity(
                    category = category,
                    monthYear = currentMonthYear,
                    monthlyLimit = limit
                )
            )
        }
    }

    // Shopping List Operations
    fun addShoppingItem(
        name: String,
        category: String = "food_groceries",
        quantity: String = "1",
        estimatedPrice: Double = 0.0
    ) {
        viewModelScope.launch {
            repository.insertShoppingItem(
                ShoppingItemEntity(
                    name = name.trim(),
                    category = category,
                    quantity = quantity.trim(),
                    estimatedPrice = estimatedPrice
                )
            )
        }
    }

    fun toggleShoppingItemBought(item: ShoppingItemEntity, actualCost: Double? = null) {
        viewModelScope.launch {
            val updatedBought = !item.isBought
            val actual = if (updatedBought && (actualCost == null || actualCost <= 0.0)) item.estimatedPrice else (actualCost ?: item.actualPrice)
            repository.updateShoppingItem(
                item.copy(isBought = updatedBought, actualPrice = actual)
            )
        }
    }

    fun logShoppingItemAsExpense(item: ShoppingItemEntity) {
        viewModelScope.launch {
            val cost = if (item.actualPrice > 0) item.actualPrice else item.estimatedPrice
            repository.insertExpense(
                ExpenseEntity(
                    title = item.name,
                    amount = cost,
                    type = "EXPENSE",
                    category = item.category,
                    notes = "Shopping list item: ${item.quantity}"
                )
            )
            repository.updateShoppingItem(item.copy(isBought = true, actualPrice = cost))
        }
    }

    fun deleteShoppingItem(item: ShoppingItemEntity) {
        viewModelScope.launch {
            repository.deleteShoppingItem(item)
        }
    }

    fun clearPurchasedShoppingItems() {
        viewModelScope.launch {
            repository.clearPurchasedShoppingItems()
        }
    }

    // Favorite Presets
    fun addFavoritePreset(
        title: String,
        amount: Double,
        category: String,
        paymentMethod: String = "UPI / GPay"
    ) {
        viewModelScope.launch {
            repository.insertFavoritePreset(
                FavoritePresetEntity(
                    title = title.trim(),
                    amount = amount,
                    category = category,
                    paymentMethod = paymentMethod
                )
            )
        }
    }

    fun recordFromPreset(preset: FavoritePresetEntity) {
        viewModelScope.launch {
            repository.insertExpense(
                ExpenseEntity(
                    title = preset.title,
                    amount = preset.amount,
                    type = preset.type,
                    category = preset.category,
                    paymentMethod = preset.paymentMethod,
                    notes = "Quick 1-tap recorded from preset"
                )
            )
        }
    }

    fun deleteFavoritePreset(preset: FavoritePresetEntity) {
        viewModelScope.launch {
            repository.deleteFavoritePreset(preset)
        }
    }

    // Voice Parser
    fun parseVoice(text: String): ParsedVoiceExpense? {
        return VoiceExpenseParser.parse(text)
    }
}
