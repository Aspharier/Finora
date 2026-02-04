package com.aspharier.finora.ui.screens.statistics

enum class StatsMode { INCOME, SPENDING }
enum class TimeRange { DAY, WEEK, MONTH, YEAR }

data class BarData(
    val label: String,
    val value: Float
)

data class  StatisticsState(
    val mode: StatsMode = StatsMode.SPENDING,
    val timeRange: TimeRange = TimeRange.MONTH,
    val totalAmount: String = "₹ 18,340",
    val isPositiveTrend: Boolean = false,
    val bars: List<BarData> = sampleBars()
)

private fun sampleBars() = listOf(
    BarData("W1", 400f),
    BarData("W2", 650f),
    BarData("W3", 520f),
    BarData("W4", 780f)
)