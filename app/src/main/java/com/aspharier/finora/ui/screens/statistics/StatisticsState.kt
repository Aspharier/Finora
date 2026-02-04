package com.aspharier.finora.ui.screens.statistics

import com.aspharier.finora.domain.model.CategorySummary
import com.aspharier.finora.domain.model.MonthlyBudget

data class StatisticsState(
    val selectedRange: TimeRange = TimeRange.MONTH,
    val totalSpent: Double = 0.0,
    val dailyAverage: Double = 0.0,
    val categoryBreakdown: List<CategorySummary> = emptyList(),
    val budgetStatus: MonthlyBudget? = null,
    val chartData: List<Pair<String, Double>> = emptyList(),
    val isLoading: Boolean = false
)

enum class TimeRange(val label: String) {
    DAY("Day"),
    WEEK("Week"),
    MONTH("Month"),
    YEAR("Year")
}