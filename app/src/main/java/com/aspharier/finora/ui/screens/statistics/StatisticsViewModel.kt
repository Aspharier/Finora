package com.aspharier.finora.ui.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspharier.finora.domain.usecase.GetBudgetStatusUseCase
import com.aspharier.finora.domain.usecase.GetExpenseStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getExpenseStatsUseCase: GetExpenseStatsUseCase,
    private val getBudgetStatusUseCase: GetBudgetStatusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticsState())
    val state: StateFlow<StatisticsState> = _state.asStateFlow()

    init {
        loadData()
        observeBudget()
    }

    private fun observeBudget() {
        viewModelScope.launch {
            getBudgetStatusUseCase().collect { budget ->
                _state.update { it.copy(budgetStatus = budget) }
            }
        }
    }

    fun onTimeRangeSelected(range: TimeRange) {
        _state.update { it.copy(selectedRange = range) }
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            val (start, end) = getDateRange(_state.value.selectedRange)
            
            val totalSpent = getExpenseStatsUseCase.getTotalSpent(start, end)
            val dailyAverage = getExpenseStatsUseCase.getDailyAverage(start, end)
            val breakdown = getExpenseStatsUseCase.getCategoryBreakdown(start, end)
            val chartData = getExpenseStatsUseCase.getChartData(start, end)
            
            _state.update { 
                it.copy(
                    isLoading = false,
                    totalSpent = totalSpent,
                    dailyAverage = dailyAverage,
                    categoryBreakdown = breakdown,
                    chartData = chartData
                ) 
            }
        }
    }

    private fun getDateRange(range: TimeRange): Pair<LocalDate, LocalDate> {
        val today = LocalDate.now()
        return when (range) {
            TimeRange.DAY -> Pair(today, today)
            TimeRange.WEEK -> {
                val start = today.minusDays(6)
                Pair(start, today)
            }
            TimeRange.MONTH -> {
                val start = today.withDayOfMonth(1)
                val end = today.with(TemporalAdjusters.lastDayOfMonth())
                Pair(start, end)
            }
            TimeRange.YEAR -> {
                val start = today.withDayOfYear(1)
                val end = today.with(TemporalAdjusters.lastDayOfYear())
                Pair(start, end)
            }
        }
    }
}