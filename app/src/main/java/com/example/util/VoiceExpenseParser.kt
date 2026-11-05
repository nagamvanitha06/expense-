package com.example.util

import com.example.data.model.ExpenseCategory
import com.example.data.model.TransactionType

data class ParsedVoiceExpense(
    val title: String,
    val amount: Double,
    val category: ExpenseCategory,
    val type: TransactionType = TransactionType.EXPENSE,
    val confidence: Boolean = true
)

object VoiceExpenseParser {

    fun parse(rawText: String): ParsedVoiceExpense? {
        val trimmed = rawText.trim()
        if (trimmed.isBlank()) return null

        // 1. Extract numbers/amount
        val numberRegex = Regex("(\\d+([.,]\\d{1,2})?)")
        val match = numberRegex.find(trimmed)
        val amount = match?.value?.replace(",", "")?.toDoubleOrNull() ?: 0.0

        // 2. Determine Income vs Expense
        val lower = trimmed.lowercase()
        val isIncome = lower.contains("salary") || lower.contains("income") ||
                lower.contains("credited") || lower.contains("bonus") ||
                lower.contains("freelance") || lower.contains("वेतन") ||
                lower.contains("कमाई") || lower.contains("జీతం") ||
                lower.contains("ఆదాయం")

        // 3. Category matching in EN, HI, TE
        val category = when {
            // Food & Groceries
            lower.contains("vegetable") || lower.contains("grocery") || lower.contains("groceries") ||
                    lower.contains("milk") || lower.contains("curd") || lower.contains("fruit") ||
                    lower.contains("sabzi") || lower.contains("सब्जी") || lower.contains("दूध") ||
                    lower.contains("राशन") || lower.contains("किराना") || lower.contains("కూరగాయలు") ||
                    lower.contains("పాలు") || lower.contains("కిరాణా") -> ExpenseCategory.FOOD_GROCERIES

            // Dining
            lower.contains("dinner") || lower.contains("lunch") || lower.contains("breakfast") ||
                    lower.contains("coffee") || lower.contains("tea") || lower.contains("hotel") ||
                    lower.contains("restaurant") || lower.contains("swiggy") || lower.contains("zomato") ||
                    lower.contains("खाना") || lower.contains("चाय") || lower.contains("नाश्ता") ||
                    lower.contains("టిఫిన్") || lower.contains("భోజనం") || lower.contains("టీ") -> ExpenseCategory.DINING

            // Transport & Fuel
            lower.contains("petrol") || lower.contains("diesel") || lower.contains("fuel") ||
                    lower.contains("uber") || lower.contains("ola") || lower.contains("cab") ||
                    lower.contains("auto") || lower.contains("bus") || lower.contains("metro") ||
                    lower.contains("पेट्रोल") || lower.contains("डीजल") || lower.contains("किराया") ||
                    lower.contains("పెట్రోల్") || lower.contains("డీజిల్") || lower.contains("ఆటో") -> ExpenseCategory.TRANSPORT_FUEL

            // Utilities & Bills
            lower.contains("bill") || lower.contains("electricity") || lower.contains("wifi") ||
                    lower.contains("internet") || lower.contains("water") || lower.contains("recharge") ||
                    lower.contains("बिजली") || lower.contains("बिल") || lower.contains("కరెంట్") ||
                    lower.contains("రీఛార్జ్") -> ExpenseCategory.UTILITIES_BILLS

            // Rent & Housing
            lower.contains("rent") || lower.contains("flat") || lower.contains("house") ||
                    lower.contains("मकान") || lower.contains("ఇంటి అద్దె") -> ExpenseCategory.RENT_HOUSING

            // Shopping
            lower.contains("shopping") || lower.contains("cloth") || lower.contains("shoes") ||
                    lower.contains("amazon") || lower.contains("flipkart") || lower.contains("कपड़े") ||
                    lower.contains("షాపింగ్") || lower.contains("బట్టలు") -> ExpenseCategory.SHOPPING

            // Health & Medical
            lower.contains("medicine") || lower.contains("doctor") || lower.contains("hospital") ||
                    lower.contains("pharmacy") || lower.contains("दवाई") || lower.contains("डॉक्टर") ||
                    lower.contains("మందులు") || lower.contains("డాక్టర్") -> ExpenseCategory.HEALTH_MEDICAL

            // Entertainment
            lower.contains("movie") || lower.contains("cinema") || lower.contains("netflix") ||
                    lower.contains("game") || lower.contains("फिल्म") || lower.contains("సినిమా") -> ExpenseCategory.ENTERTAINMENT

            // Education
            lower.contains("book") || lower.contains("fees") || lower.contains("school") ||
                    lower.contains("college") || lower.contains("फीस") || lower.contains("పుస్తకాలు") -> ExpenseCategory.EDUCATION

            // Salary / Business
            isIncome && (lower.contains("salary") || lower.contains("वेतन") || lower.contains("జీతం")) -> ExpenseCategory.SALARY
            isIncome -> ExpenseCategory.BUSINESS

            else -> ExpenseCategory.OTHER
        }

        // 4. Construct title by cleaning amount and common trigger words
        var cleanedTitle = trimmed
            .replace(Regex("(?i)\\b(spent|paid|rs|rupees|rupee|for|on|in|to|का|की|के|రూపాయలు|రూ)\\b"), "")
            .replace(Regex("\\d+([.,]\\d{1,2})?"), "")
            .replace(Regex("\\s+"), " ")
            .trim()

        if (cleanedTitle.isBlank() || cleanedTitle.length < 2) {
            cleanedTitle = when (category) {
                ExpenseCategory.FOOD_GROCERIES -> "Grocery & Food Purchase"
                ExpenseCategory.TRANSPORT_FUEL -> "Fuel / Transport"
                ExpenseCategory.DINING -> "Food & Dining"
                ExpenseCategory.UTILITIES_BILLS -> "Utility Bill Payment"
                ExpenseCategory.SHOPPING -> "Shopping Item"
                ExpenseCategory.HEALTH_MEDICAL -> "Pharmacy / Medical"
                ExpenseCategory.SALARY -> "Monthly Salary"
                else -> if (isIncome) "Income" else "Expense"
            }
        }

        return ParsedVoiceExpense(
            title = cleanedTitle.replaceFirstChar { it.uppercase() },
            amount = amount,
            category = category,
            type = if (isIncome) TransactionType.INCOME else TransactionType.EXPENSE,
            confidence = amount > 0
        )
    }
}
