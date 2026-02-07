package com.aspharier.finora.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aspharier.finora.domain.model.Category
import com.aspharier.finora.domain.model.CategorySummary
import com.aspharier.finora.domain.model.Expense
import com.aspharier.finora.domain.model.MonthlyBudget
import com.aspharier.finora.ui.theme.NeonGreen
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
        viewModel: HomeViewModel = hiltViewModel(),
        onToggleTheme: () -> Unit = {},
        isDarkTheme: Boolean = true
) {
        val state by viewModel.state.collectAsState()
        var showBudgetDialog by remember { mutableStateOf(false) }

        if (showBudgetDialog) {
                SetBudgetBottomSheet(
                        onDismiss = { showBudgetDialog = false },
                        onSave = { amount ->
                                viewModel.setBudget(amount)
                                showBudgetDialog = false
                        }
                )
        }

        Scaffold(
                topBar = { HomeTopBar(onToggleTheme = onToggleTheme, isDarkTheme = isDarkTheme) }
        ) { padding ->
                if (state.isLoading) {
                        Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator(color = NeonGreen) }
                } else {
                        LazyColumn(
                                modifier =
                                        Modifier.fillMaxSize()
                                                .padding(padding)
                                                .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                                item {
                                        MonthlyBudgetCard(
                                                budget = state.budgetStatus,
                                                dailyAverage = state.dailyAverage,
                                                onSetBudgetClick = { showBudgetDialog = true },
                                                onResetBudgetClick = { viewModel.resetBudget() }
                                        )
                                }

                                if (state.topCategories.isNotEmpty()) {
                                        item { CategoryBreakdownPreview(state.topCategories) }
                                }

                                item { RecentExpensesSection(state.recentExpenses) }

                                item {
                                        Spacer(
                                                modifier = Modifier.height(80.dp)
                                        ) // Bottom padding for nav bar
                                }
                        }
                }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(onToggleTheme: () -> Unit, isDarkTheme: Boolean) {
        TopAppBar(
                title = {
                        Text(
                                text = "Finora",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                        )
                },
                actions = {
                        IconButton(onClick = onToggleTheme) {
                                Icon(
                                        imageVector =
                                                if (isDarkTheme) Icons.Default.LightMode
                                                else Icons.Default.DarkMode,
                                        contentDescription = "Toggle Theme"
                                )
                        }
                },
                colors =
                        TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background
                        ),
                windowInsets = WindowInsets(top = 0.dp)
        )
}

@Composable
private fun MonthlyBudgetCard(
        budget: MonthlyBudget?,
        dailyAverage: Double,
        onSetBudgetClick: () -> Unit,
        onResetBudgetClick: () -> Unit
) {
        Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                        CardDefaults.cardColors(
                                containerColor =
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                shape = RoundedCornerShape(24.dp)
        ) {
                Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Text(
                                        text = "Monthly Budget",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (budget != null) {
                                        IconButton(onClick = onResetBudgetClick) {
                                                Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Reset Budget",
                                                        tint = MaterialTheme.colorScheme.error
                                                )
                                        }
                                }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (budget == null) {
                                Button(
                                        onClick = onSetBudgetClick,
                                        colors =
                                                ButtonDefaults.buttonColors(
                                                        containerColor = NeonGreen
                                                ),
                                        modifier = Modifier.fillMaxWidth()
                                ) { Text("Set Budget", color = Color.Black) }
                        } else {
                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.Bottom
                                ) {
                                        Text(
                                                text = "₹ ${String.format("%.0f", budget.spent)}",
                                                style = MaterialTheme.typography.displayMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                                text = "/ ${String.format("%.0f", budget.amount)}",
                                                style = MaterialTheme.typography.titleLarge,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(bottom = 6.dp)
                                        )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                LinearProgressIndicator(
                                        progress = { budget.percentageUsed / 100f },
                                        modifier =
                                                Modifier.fillMaxWidth()
                                                        .height(12.dp)
                                                        .clip(RoundedCornerShape(6.dp)),
                                        color =
                                                when {
                                                        budget.percentageUsed > 100 ->
                                                                MaterialTheme.colorScheme.error
                                                        budget.percentageUsed > 80 ->
                                                                Color(0xFFFFB74D)
                                                        else -> NeonGreen
                                                },
                                        trackColor = MaterialTheme.colorScheme.surface,
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                        Text(
                                                text =
                                                        "Remaining: ₹ ${String.format("%.0f", budget.remaining)}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        Text(
                                                text =
                                                        "Avg: ₹ ${String.format("%.0f", dailyAverage)} / day",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                }
                        }
                }
        }
}

@Composable
private fun CategoryBreakdownPreview(categories: List<CategorySummary>) {
        Column {
                Text(
                        text = "Top Spending",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                        categories.forEach { summary ->
                                CategoryPreviewItem(
                                        summary = summary,
                                        modifier = Modifier.weight(1f)
                                )
                        }
                }
        }
}

@Composable
private fun CategoryPreviewItem(summary: CategorySummary, modifier: Modifier = Modifier) {
        Column(
                modifier =
                        modifier.clip(RoundedCornerShape(16.dp))
                                .background(
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                )
                                .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Icon(
                        imageVector = Category.getIconForName(summary.category.iconName),
                        contentDescription = null,
                        tint = Color(android.graphics.Color.parseColor(summary.category.colorHex)),
                        modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                        text = summary.category.name,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                        text = "₹${String.format("%.0f", summary.amount)}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
        }
}

@Composable
private fun RecentExpensesSection(expenses: List<Expense>) {
        Column {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        Text(
                                text = "Recent Expenses",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                        )
                        //                        TextButton(onClick = { /* TODO: View All */}) {
                        //                                Text("See All", color = NeonGreen)
                        //                        }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (expenses.isEmpty()) {
                        Box(
                                modifier = Modifier.fillMaxWidth().padding(32.dp),
                                contentAlignment = Alignment.Center
                        ) {
                                Text(
                                        text = "No expenses yet",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                        }
                } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                expenses.forEach { expense -> ExpenseListItem(expense) }
                        }
                }
        }
}

@Composable
private fun ExpenseListItem(expense: Expense) {
        Row(
                modifier =
                        Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable { /* TODO: Edit expense */}
                                .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
                // Icon
                Box(
                        modifier =
                                Modifier.size(48.dp)
                                        .clip(CircleShape)
                                        .background(
                                                Color(
                                                                android.graphics.Color.parseColor(
                                                                        expense.category.colorHex
                                                                )
                                                        )
                                                        .copy(alpha = 0.2f)
                                        ),
                        contentAlignment = Alignment.Center
                ) {
                        Icon(
                                imageVector = Category.getIconForName(expense.category.iconName),
                                contentDescription = null,
                                tint =
                                        Color(
                                                android.graphics.Color.parseColor(
                                                        expense.category.colorHex
                                                )
                                        ),
                                modifier = Modifier.size(24.dp)
                        )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Details
                Column(modifier = Modifier.weight(1f)) {
                        Text(
                                text = expense.category.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                        )
                        if (expense.note.isNotEmpty()) {
                                Text(
                                        text = expense.note,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1
                                )
                        }
                }

                // Amount & Date
                Column(horizontalAlignment = Alignment.End) {
                        Text(
                                text = "- ₹${String.format("%.0f", expense.amount)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                        )
                        Text(
                                text = expense.date.format(DateTimeFormatter.ofPattern("MMM dd")),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetBudgetBottomSheet(onDismiss: () -> Unit, onSave: (Double) -> Unit) {
        val sheetState = rememberModalBottomSheetState()
        var amount by remember { mutableStateOf("") }

        ModalBottomSheet(
                onDismissRequest = onDismiss,
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface
        ) {
                Column(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .padding(horizontal = 24.dp)
                                        .padding(bottom = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Text(
                                text = "Set Monthly Budget",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                                value = amount,
                                onValueChange = {
                                        if (it.all { char -> char.isDigit() || char == '.' })
                                                amount = it
                                },
                                label = { Text("Amount") },
                                prefix = { Text("₹") },
                                keyboardOptions =
                                        androidx.compose.foundation.text.KeyboardOptions(
                                                keyboardType =
                                                        androidx.compose.ui.text.input.KeyboardType
                                                                .Number
                                        ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors =
                                        OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = NeonGreen,
                                                focusedLabelColor = NeonGreen
                                        )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                                onClick = {
                                        amount.toDoubleOrNull()?.let {
                                                onSave(it)
                                                onDismiss()
                                        }
                                },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                                enabled = amount.isNotEmpty() && amount.toDoubleOrNull() != null
                        ) {
                                Text(
                                        text = "Save Budget",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                )
                        }
                }
        }
}
