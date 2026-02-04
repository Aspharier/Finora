package com.aspharier.finora.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.aspharier.finora.domain.model.StatisticResult
import com.aspharier.finora.domain.model.TransactionType
import com.aspharier.finora.domain.repository.TransactionRepository
import com.aspharier.finora.ui.screens.statistics.StatsMode
import com.aspharier.finora.ui.screens.statistics.TimeRange
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

class GetStatisticsUseCase (
    private val repository: TransactionRepository
){
    @RequiresApi(Build.VERSION_CODES.O)
    fun execute(
        mode: StatsMode,
        range: TimeRange
    ): StatisticResult {
        val type = if (mode == StatsMode.INCOME)
            TransactionType.INCOME
        else
            TransactionType.EXPENSE

        val today = LocalDate.now()

        val (start, end) = when (range) {
            TimeRange.MONTH -> today.withDayOfMonth(1) to today
            TimeRange.WEEK -> today.minusDays(6) to today
            TimeRange.YEAR -> today.withDayOfYear(1) to today
            else -> today to today
        }

        return repository.getStatistics(type, start, end)
    }
}