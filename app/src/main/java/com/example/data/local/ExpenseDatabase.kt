package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.BudgetEntity
import com.example.data.model.ExpenseEntity
import com.example.data.model.FavoritePresetEntity
import com.example.data.model.ShoppingItemEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

@Database(
    entities = [
        ExpenseEntity::class,
        BudgetEntity::class,
        ShoppingItemEntity::class,
        FavoritePresetEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_tracker_db"
                )
                    .addCallback(ExpenseDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class ExpenseDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.expenseDao())
                    }
                }
            }

            private suspend fun populateInitialData(dao: ExpenseDao) {
                val now = System.currentTimeMillis()
                val oneDay = 86400000L

                // Initial realistic transactions
                val sampleExpenses = listOf(
                    ExpenseEntity(
                        title = "Monthly Salary",
                        amount = 65000.0,
                        type = "INCOME",
                        category = "salary",
                        timestamp = now - (oneDay * 4),
                        paymentMethod = "Bank Transfer",
                        notes = "November Salary credited"
                    ),
                    ExpenseEntity(
                        title = "Supermarket Groceries & Veggies",
                        amount = 2450.0,
                        type = "EXPENSE",
                        category = "food_groceries",
                        timestamp = now - (oneDay * 3),
                        paymentMethod = "UPI / GPay",
                        notes = "Rice, Dal, cooking oil, and vegetables"
                    ),
                    ExpenseEntity(
                        title = "House Rent",
                        amount = 14000.0,
                        type = "EXPENSE",
                        category = "rent_housing",
                        timestamp = now - (oneDay * 3),
                        paymentMethod = "Bank Transfer",
                        notes = "Apartment monthly rent"
                    ),
                    ExpenseEntity(
                        title = "Two-Wheeler Petrol / Fuel",
                        amount = 650.0,
                        type = "EXPENSE",
                        category = "transport_fuel",
                        timestamp = now - (oneDay * 2),
                        paymentMethod = "UPI / GPay",
                        notes = "Full tank petrol"
                    ),
                    ExpenseEntity(
                        title = "South Indian Family Dinner",
                        amount = 1280.0,
                        type = "EXPENSE",
                        category = "dining",
                        timestamp = now - (oneDay * 1),
                        paymentMethod = "Card",
                        notes = "Biryani & Starters with friends"
                    ),
                    ExpenseEntity(
                        title = "Electricity & Wi-Fi Bill",
                        amount = 1850.0,
                        type = "EXPENSE",
                        category = "utilities_bills",
                        timestamp = now - (3600000L * 14),
                        paymentMethod = "UPI / GPay",
                        notes = "Broadband and electricity"
                    ),
                    ExpenseEntity(
                        title = "Freelance Project Milestone",
                        amount = 12000.0,
                        type = "INCOME",
                        category = "business",
                        timestamp = now - (3600000L * 6),
                        paymentMethod = "Bank Transfer",
                        notes = "App consultation payout"
                    ),
                    ExpenseEntity(
                        title = "Pharmacy & Medicines",
                        amount = 420.0,
                        type = "EXPENSE",
                        category = "health_medical",
                        timestamp = now - (3600000L * 2),
                        paymentMethod = "Cash",
                        notes = "Vitamins and first aid"
                    )
                )
                dao.insertExpenses(sampleExpenses)

                // Current Month Budgets
                val cal = Calendar.getInstance()
                val monthYear = String.format("%04d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1)
                dao.insertOrUpdateBudget(BudgetEntity(category = "TOTAL", monthYear = monthYear, monthlyLimit = 35000.0))
                dao.insertOrUpdateBudget(BudgetEntity(category = "food_groceries", monthYear = monthYear, monthlyLimit = 8000.0))
                dao.insertOrUpdateBudget(BudgetEntity(category = "dining", monthYear = monthYear, monthlyLimit = 4000.0))
                dao.insertOrUpdateBudget(BudgetEntity(category = "transport_fuel", monthYear = monthYear, monthlyLimit = 3000.0))
                dao.insertOrUpdateBudget(BudgetEntity(category = "utilities_bills", monthYear = monthYear, monthlyLimit = 3500.0))

                // Initial Favorite 1-Tap Presets
                dao.insertFavoritePreset(FavoritePresetEntity(title = "Morning Filter Coffee", amount = 30.0, category = "dining", paymentMethod = "UPI / GPay"))
                dao.insertFavoritePreset(FavoritePresetEntity(title = "Milk & Bread", amount = 65.0, category = "food_groceries", paymentMethod = "Cash"))
                dao.insertFavoritePreset(FavoritePresetEntity(title = "Metro Rail / Bus Pass", amount = 50.0, category = "transport_fuel", paymentMethod = "UPI / GPay"))
                dao.insertFavoritePreset(FavoritePresetEntity(title = "Office Thali Lunch", amount = 120.0, category = "dining", paymentMethod = "UPI / GPay"))
                dao.insertFavoritePreset(FavoritePresetEntity(title = "Petrol Top-up", amount = 500.0, category = "transport_fuel", paymentMethod = "UPI / GPay"))

                // Initial Shopping items
                dao.insertShoppingItem(ShoppingItemEntity(name = "Basmati Rice (5kg)", category = "food_groceries", quantity = "5 kg", estimatedPrice = 450.0))
                dao.insertShoppingItem(ShoppingItemEntity(name = "Sunflower Cooking Oil", category = "food_groceries", quantity = "2 L", estimatedPrice = 310.0))
                dao.insertShoppingItem(ShoppingItemEntity(name = "Fresh Tomatoes & Onions", category = "food_groceries", quantity = "3 kg", estimatedPrice = 140.0, isBought = true, actualPrice = 135.0))
                dao.insertShoppingItem(ShoppingItemEntity(name = "Dishwasher Soap & Sponge", category = "utilities_bills", quantity = "1 pack", estimatedPrice = 180.0))
            }
        }
    }
}
