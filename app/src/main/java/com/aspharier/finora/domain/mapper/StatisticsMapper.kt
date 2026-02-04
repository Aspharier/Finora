package com.aspharier.finora.domain.mapper

import com.aspharier.finora.domain.model.StatisticResult
import com.aspharier.finora.ui.screens.statistics.StatisticsState

object StatisticsMapper {
    fun toUi(result: StatisticResult): StatisticsState {
        return StatisticsState(
            totalSpent = result.totalAmount,
            chartData = result.bars
        )
    }
}
