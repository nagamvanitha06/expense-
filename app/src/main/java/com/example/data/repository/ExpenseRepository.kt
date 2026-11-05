package com.example.data.repository

import com.example.data.local.ExpenseDao
import com.example.data.model.BudgetEntity
import com.example.data.model.ExpenseEntity
import com.example.data.model.FavoritePresetEntity
import com.example.data.model.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val dao: ExpenseDao) {

    val allExpenses: Flow<List<ExpenseEntity>> = dao.getAllExpenses()

    fun getExpensesBetween(start: Long, end: Long): Flow<List<ExpenseEntity>> =
        dao.getExpensesBetween(start, end)

    fun searchExpenses(query: String): Flow<List<ExpenseEntity>> =
        dao.searchExpenses(query)

    fun getFavoriteExpenses(): Flow<List<ExpenseEntity>> =
        dao.getFavoriteExpenses()

    suspend fun insertExpense(expense: ExpenseEntity): Long =
        dao.insertExpense(expense)

    suspend fun updateExpense(expense: ExpenseEntity) =
        dao.updateExpense(expense)

    suspend fun deleteExpense(expense: ExpenseEntity) =
        dao.deleteExpense(expense)

    suspend fun deleteExpenseById(id: Long) =
        dao.deleteExpenseById(id)

    // Budgets
    fun getBudgetsForMonth(monthYear: String): Flow<List<BudgetEntity>> =
        dao.getBudgetsForMonth(monthYear)

    suspend fun setBudget(budget: BudgetEntity) =
        dao.insertOrUpdateBudget(budget)

    suspend fun deleteBudget(category: String, monthYear: String) =
        dao.deleteBudget(category, monthYear)

    // Shopping List
    val allShoppingItems: Flow<List<ShoppingItemEntity>> = dao.getAllShoppingItems()

    suspend fun insertShoppingItem(item: ShoppingItemEntity): Long =
        dao.insertShoppingItem(item)

    suspend fun updateShoppingItem(item: ShoppingItemEntity) =
        dao.updateShoppingItem(item)

    suspend fun deleteShoppingItem(item: ShoppingItemEntity) =
        dao.deleteShoppingItem(item)

    suspend fun clearPurchasedShoppingItems() =
        dao.deletePurchasedShoppingItems()

    // Favorite Presets
    val allFavoritePresets: Flow<List<FavoritePresetEntity>> = dao.getAllFavoritePresets()

    suspend fun insertFavoritePreset(preset: FavoritePresetEntity): Long =
        dao.insertFavoritePreset(preset)

    suspend fun deleteFavoritePreset(preset: FavoritePresetEntity) =
        dao.deleteFavoritePreset(preset)
}
