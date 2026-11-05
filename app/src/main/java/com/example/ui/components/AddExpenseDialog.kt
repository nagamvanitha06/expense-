package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.model.AppLanguage
import com.example.data.model.ExpenseCategory
import com.example.data.model.TransactionType
import com.example.util.Localization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    language: AppLanguage,
    currencySymbol: String,
    initialType: TransactionType = TransactionType.EXPENSE,
    onDismiss: () -> Unit,
    onSave: (title: String, amount: Double, type: TransactionType, category: ExpenseCategory, paymentMethod: String, notes: String) -> Unit
) {
    var selectedType by remember { mutableStateOf(initialType) }
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var selectedCategory by remember {
        mutableStateOf(if (initialType == TransactionType.INCOME) ExpenseCategory.SALARY else ExpenseCategory.FOOD_GROCERIES)
    }
    var paymentMethod by remember { mutableStateOf("UPI / GPay") }
    var notes by remember { mutableStateOf("") }
    var hasError by remember { mutableStateOf(false) }

    val paymentOptions = listOf("UPI / GPay", "Cash", "Card", "Bank Transfer")

    val categories = if (selectedType == TransactionType.EXPENSE) {
        listOf(
            ExpenseCategory.FOOD_GROCERIES,
            ExpenseCategory.DINING,
            ExpenseCategory.TRANSPORT_FUEL,
            ExpenseCategory.SHOPPING,
            ExpenseCategory.UTILITIES_BILLS,
            ExpenseCategory.RENT_HOUSING,
            ExpenseCategory.HEALTH_MEDICAL,
            ExpenseCategory.ENTERTAINMENT,
            ExpenseCategory.EDUCATION,
            ExpenseCategory.INVESTMENT,
            ExpenseCategory.OTHER
        )
    } else {
        listOf(
            ExpenseCategory.SALARY,
            ExpenseCategory.BUSINESS,
            ExpenseCategory.INVESTMENT,
            ExpenseCategory.OTHER
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Localization.s("new_transaction", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Type Switcher Tabs
                TabRow(
                    selectedTabIndex = if (selectedType == TransactionType.EXPENSE) 0 else 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedType == TransactionType.EXPENSE,
                        onClick = {
                            selectedType = TransactionType.EXPENSE
                            selectedCategory = ExpenseCategory.FOOD_GROCERIES
                        },
                        text = {
                            Text(
                                text = Localization.s("filter_expense", language),
                                fontWeight = FontWeight.SemiBold,
                                color = if (selectedType == TransactionType.EXPENSE) Color(0xFFEF4444) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                    Tab(
                        selected = selectedType == TransactionType.INCOME,
                        onClick = {
                            selectedType = TransactionType.INCOME
                            selectedCategory = ExpenseCategory.SALARY
                        },
                        text = {
                            Text(
                                text = Localization.s("filter_income", language),
                                fontWeight = FontWeight.SemiBold,
                                color = if (selectedType == TransactionType.INCOME) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        hasError = false
                    },
                    label = { Text(Localization.s("title", language)) },
                    placeholder = { Text(Localization.s("title_hint", language)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_transaction_title_input"),
                    singleLine = true,
                    isError = hasError && title.isBlank(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Amount Input
                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it
                        hasError = false
                    },
                    label = { Text(Localization.s("amount", language)) },
                    prefix = { Text("$currencySymbol ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_transaction_amount_input"),
                    singleLine = true,
                    isError = hasError && (amountText.toDoubleOrNull() ?: 0.0) <= 0.0,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category Selection
                Text(
                    text = Localization.s("category", language),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            label = { Text(Localization.getCategoryName(cat, language)) },
                            leadingIcon = {
                                CategoryIconBadge(category = cat, size = 24.dp, iconSize = 14.dp)
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Payment Method
                Text(
                    text = Localization.s("payment_method", language),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    paymentOptions.forEach { method ->
                        val isSelected = paymentMethod == method
                        FilterChip(
                            selected = isSelected,
                            onClick = { paymentMethod = method },
                            label = { Text(method) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(Localization.s("notes", language)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_transaction_notes_input"),
                    maxLines = 2,
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    if (title.isNotBlank() && amount > 0.0) {
                        onSave(title, amount, selectedType, selectedCategory, paymentMethod, notes)
                        onDismiss()
                    } else {
                        hasError = true
                    }
                },
                modifier = Modifier.testTag("save_transaction_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = Localization.s("save", language))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = Localization.s("cancel", language))
            }
        }
    )
}
