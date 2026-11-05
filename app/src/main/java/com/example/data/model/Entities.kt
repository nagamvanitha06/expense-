package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val type: String = "EXPENSE", // "EXPENSE" or "INCOME"
    val category: String = "other",
    val timestamp: Long = System.currentTimeMillis(),
    val paymentMethod: String = "UPI / GPay",
    val notes: String = "",
    val isFavorite: Boolean = false
)

@Entity(tableName = "budgets", primaryKeys = ["category", "monthYear"])
data class BudgetEntity(
    val category: String, // e.g. "TOTAL" or category id like "food_groceries"
    val monthYear: String, // e.g. "2026-11"
    val monthlyLimit: Double
)

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String = "food_groceries",
    val quantity: String = "1",
    val estimatedPrice: Double = 0.0,
    val actualPrice: Double = 0.0,
    val isBought: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorite_presets")
data class FavoritePresetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val type: String = "EXPENSE",
    val paymentMethod: String = "UPI / GPay"
)
