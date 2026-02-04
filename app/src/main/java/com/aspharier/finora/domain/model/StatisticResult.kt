package com.aspharier.finora.domain.model

data class StatisticResult(
    val totalAmount: Double,
    val isPositiveTrend: Boolean,
    val breakdown: Map<String, Double>,
    val bars: List<Pair<String, Double>>
)
