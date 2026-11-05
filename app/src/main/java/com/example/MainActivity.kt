package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.ExpenseDatabase
import com.example.data.model.AppLanguage
import com.example.data.model.ExpenseEntity
import com.example.data.model.TransactionType
import com.example.data.repository.ExpenseRepository
import com.example.ui.ExpenseViewModel
import com.example.ui.components.AddExpenseDialog
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.ScreenTab
import com.example.ui.components.SetBudgetDialog
import com.example.ui.components.VoiceExpenseDialog
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ShoppingPlannerScreen
import com.example.ui.screens.TransactionsScreen
import com.example.ui.theme.ExpenseTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ExpenseTrackerTheme {
                ExpenseTrackerApp()
            }
        }
    }
}

@Composable
fun ExpenseTrackerApp() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Initialize Database & Repository
    val database = remember { ExpenseDatabase.getDatabase(context, coroutineScope) }
    val repository = remember { ExpenseRepository(database.expenseDao()) }

    // Initialize ViewModel
    val viewModel = remember { ExpenseViewModel(repository) }

    // Reactive State
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val currencySymbol by viewModel.currencySymbol.collectAsStateWithLifecycle()
    val allExpenses by viewModel.allExpenses.collectAsStateWithLifecycle()
    val filteredExpenses by viewModel.filteredExpenses.collectAsStateWithLifecycle()
    val budgets by viewModel.budgets.collectAsStateWithLifecycle()
    val shoppingItems by viewModel.shoppingItems.collectAsStateWithLifecycle()
    val favoritePresets by viewModel.favoritePresets.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()

    // Monthly Budget limit
    val monthlyBudgetLimit = remember(budgets) {
        budgets.firstOrNull { it.category == "TOTAL" }?.monthlyLimit ?: 35000.0
    }

    // Navigation State
    var currentTab by remember { mutableStateOf(ScreenTab.HOME) }

    // Dialog States
    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var addExpenseInitialType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var showVoiceDialog by remember { mutableStateOf(false) }
    var showSetBudgetDialog by remember { mutableStateOf(false) }

    // Back handler: return to Home tab if on another screen
    BackHandler(enabled = currentTab != ScreenTab.HOME) {
        currentTab = ScreenTab.HOME
    }

    Scaffold(
        bottomBar = {
            AppBottomNavBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it },
                language = language
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                ScreenTab.HOME -> {
                    HomeScreen(
                        expenses = allExpenses,
                        monthlyBudgetLimit = monthlyBudgetLimit,
                        language = language,
                        currencySymbol = currencySymbol,
                        onSetLanguage = { viewModel.setLanguage(it) },
                        onToggleCurrency = {
                            viewModel.setCurrency(if (currencySymbol == "₹") "$" else "₹")
                        },
                        onOpenVoiceLog = { showVoiceDialog = true },
                        onOpenAddExpense = { type ->
                            addExpenseInitialType = type
                            showAddExpenseDialog = true
                        },
                        onNavigateTab = { currentTab = it },
                        onDeleteExpense = { viewModel.deleteTransaction(it) },
                        onOpenSetBudgetDialog = { showSetBudgetDialog = true }
                    )
                }
                ScreenTab.TRANSACTIONS -> {
                    TransactionsScreen(
                        expenses = filteredExpenses,
                        searchQuery = searchQuery,
                        selectedFilter = selectedFilter,
                        language = language,
                        currencySymbol = currencySymbol,
                        onSearchChange = { viewModel.setSearchQuery(it) },
                        onFilterChange = { viewModel.setSelectedFilter(it) },
                        onDeleteExpense = { viewModel.deleteTransaction(it) }
                    )
                }
                ScreenTab.ANALYTICS -> {
                    AnalyticsScreen(
                        expenses = allExpenses,
                        monthlyBudgetLimit = monthlyBudgetLimit,
                        language = language,
                        currencySymbol = currencySymbol,
                        onOpenSetBudgetDialog = { showSetBudgetDialog = true }
                    )
                }
                ScreenTab.SHOPPING -> {
                    ShoppingPlannerScreen(
                        items = shoppingItems,
                        language = language,
                        currencySymbol = currencySymbol,
                        onAddItem = { name, qty, price ->
                            viewModel.addShoppingItem(name = name, quantity = qty, estimatedPrice = price)
                        },
                        onToggleBought = { viewModel.toggleShoppingItemBought(it) },
                        onLogAsExpense = { viewModel.logShoppingItemAsExpense(it) },
                        onDeleteItem = { viewModel.deleteShoppingItem(it) },
                        onClearPurchased = { viewModel.clearPurchasedShoppingItems() }
                    )
                }
                ScreenTab.FAVORITES -> {
                    FavoritesScreen(
                        presets = favoritePresets,
                        language = language,
                        currencySymbol = currencySymbol,
                        onRecordPreset = { viewModel.recordFromPreset(it) },
                        onAddPreset = { title, amt, cat ->
                            viewModel.addFavoritePreset(title = title, amount = amt, category = cat)
                        },
                        onDeletePreset = { viewModel.deleteFavoritePreset(it) }
                    )
                }
            }
        }
    }

    // Add / Edit Transaction Dialog
    if (showAddExpenseDialog) {
        AddExpenseDialog(
            language = language,
            currencySymbol = currencySymbol,
            initialType = addExpenseInitialType,
            onDismiss = { showAddExpenseDialog = false },
            onSave = { title, amount, type, category, paymentMethod, notes ->
                viewModel.addTransaction(
                    title = title,
                    amount = amount,
                    type = type,
                    category = category,
                    paymentMethod = paymentMethod,
                    notes = notes
                )
            }
        )
    }

    // Voice Command Dialog
    if (showVoiceDialog) {
        VoiceExpenseDialog(
            language = language,
            currencySymbol = currencySymbol,
            onDismiss = { showVoiceDialog = false },
            onConfirmExpense = { parsed ->
                viewModel.addTransaction(
                    title = parsed.title,
                    amount = parsed.amount,
                    type = parsed.type,
                    category = parsed.category,
                    notes = "Voice command: ${parsed.title}"
                )
            }
        )
    }

    // Set Monthly Budget Dialog
    if (showSetBudgetDialog) {
        SetBudgetDialog(
            currentLimit = monthlyBudgetLimit,
            language = language,
            currencySymbol = currencySymbol,
            onDismiss = { showSetBudgetDialog = false },
            onConfirm = { newLimit ->
                viewModel.setMonthlyBudget(newLimit)
            }
        )
    }
}
