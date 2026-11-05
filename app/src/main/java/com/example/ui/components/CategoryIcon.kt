package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.model.ExpenseCategory

data class CategoryVisual(
    val icon: ImageVector,
    val containerColor: Color,
    val iconColor: Color
)

fun getCategoryVisual(category: ExpenseCategory): CategoryVisual {
    return when (category) {
        ExpenseCategory.FOOD_GROCERIES -> CategoryVisual(
            icon = Icons.Default.ShoppingCart,
            containerColor = Color(0xFFDCFCE7),
            iconColor = Color(0xFF16A34A)
        )
        ExpenseCategory.DINING -> CategoryVisual(
            icon = Icons.Default.Restaurant,
            containerColor = Color(0xFFFFEDD5),
            iconColor = Color(0xFFEA580C)
        )
        ExpenseCategory.TRANSPORT_FUEL -> CategoryVisual(
            icon = Icons.Default.DirectionsCar,
            containerColor = Color(0xFFDBEAFE),
            iconColor = Color(0xFF2563EB)
        )
        ExpenseCategory.SHOPPING -> CategoryVisual(
            icon = Icons.Default.LocalMall,
            containerColor = Color(0xFFFCE7F3),
            iconColor = Color(0xFFDB2777)
        )
        ExpenseCategory.UTILITIES_BILLS -> CategoryVisual(
            icon = Icons.Default.Lightbulb,
            containerColor = Color(0xFFFEF3C7),
            iconColor = Color(0xFFD97706)
        )
        ExpenseCategory.RENT_HOUSING -> CategoryVisual(
            icon = Icons.Default.Home,
            containerColor = Color(0xFFE0E7FF),
            iconColor = Color(0xFF4F46E5)
        )
        ExpenseCategory.HEALTH_MEDICAL -> CategoryVisual(
            icon = Icons.Default.LocalHospital,
            containerColor = Color(0xFFFEE2E2),
            iconColor = Color(0xFFDC2626)
        )
        ExpenseCategory.ENTERTAINMENT -> CategoryVisual(
            icon = Icons.Default.Movie,
            containerColor = Color(0xFFEDE9FE),
            iconColor = Color(0xFF7C3AED)
        )
        ExpenseCategory.EDUCATION -> CategoryVisual(
            icon = Icons.Default.School,
            containerColor = Color(0xFFCCFBF1),
            iconColor = Color(0xFF0D9488)
        )
        ExpenseCategory.INVESTMENT -> CategoryVisual(
            icon = Icons.Default.AccountBalance,
            containerColor = Color(0xFFE0F2FE),
            iconColor = Color(0xFF0284C7)
        )
        ExpenseCategory.SALARY -> CategoryVisual(
            icon = Icons.Default.AttachMoney,
            containerColor = Color(0xFFD1FAE5),
            iconColor = Color(0xFF059669)
        )
        ExpenseCategory.BUSINESS -> CategoryVisual(
            icon = Icons.Default.Business,
            containerColor = Color(0xFFFEF9C3),
            iconColor = Color(0xFFCA8A04)
        )
        ExpenseCategory.OTHER -> CategoryVisual(
            icon = Icons.Default.Receipt,
            containerColor = Color(0xFFF1F5F9),
            iconColor = Color(0xFF64748B)
        )
    }
}

@Composable
fun CategoryIconBadge(
    category: ExpenseCategory,
    size: Dp = 44.dp,
    iconSize: Dp = 22.dp,
    modifier: Modifier = Modifier
) {
    val visual = getCategoryVisual(category)
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(visual.containerColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = visual.icon,
            contentDescription = category.name,
            tint = visual.iconColor,
            modifier = Modifier.size(iconSize)
        )
    }
}
