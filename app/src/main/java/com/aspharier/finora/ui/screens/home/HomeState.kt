package com.aspharier.finora.ui.screens.home

import com.aspharier.finora.domain.model.CategorySummary
import com.aspharier.finora.domain.model.Expense
import com.aspharier.finora.domain.model.MonthlyBudget

data class HomeState(
    val budgetStatus: MonthlyBudget? = null,
    val topCategories: List<CategorySummary> = emptyList(),
    val recentExpenses: List<Expense> = emptyList(),
    val dailyAverage: Double = 0.0,
    val isLoading: Boolean = false
)