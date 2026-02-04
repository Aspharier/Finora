package com.aspharier.finora.domain.mapper

import com.aspharier.finora.domain.model.StatisticResult
import com.aspharier.finora.ui.screens.statistics.BarData
import com.aspharier.finora.ui.screens.statistics.StatisticsState

object StatisticsMapper {
    fun toUi(result: StatisticResult): StatisticsState {
        return StatisticsState(
            totalAmount = "₹ ${"%,.0f".format(result.totalAmount)}",
            isPositiveTrend = result.isPositiveTrend,
            bars = result.bars.map {
                BarData(
                    label = it.first,
                    value = it.second.toFloat()
                )
            }
        )
    }
}