package com.example.data.model

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिंदी"),
    TELUGU("te", "Telugu", "తెలుగు")
}

enum class TransactionType {
    EXPENSE,
    INCOME
}

enum class ExpenseCategory(val id: String, val defaultType: TransactionType = TransactionType.EXPENSE) {
    FOOD_GROCERIES("food_groceries"),
    DINING("dining"),
    TRANSPORT_FUEL("transport_fuel"),
    SHOPPING("shopping"),
    UTILITIES_BILLS("utilities_bills"),
    RENT_HOUSING("rent_housing"),
    HEALTH_MEDICAL("health_medical"),
    ENTERTAINMENT("entertainment"),
    EDUCATION("education"),
    INVESTMENT("investment"),
    SALARY("salary", TransactionType.INCOME),
    BUSINESS("business", TransactionType.INCOME),
    OTHER("other");

    companion object {
        fun fromId(id: String): ExpenseCategory {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: OTHER
        }
    }
}
