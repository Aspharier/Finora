package com.aspharier.finora.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspharier.finora.domain.repository.BudgetRepository
import com.aspharier.finora.domain.repository.ExpenseRepository
import com.aspharier.finora.domain.usecase.GetBudgetStatusUseCase
import com.aspharier.finora.domain.usecase.GetExpenseStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel
@Inject
constructor(
        private val expenseRepository: ExpenseRepository,
        private val budgetRepository: BudgetRepository,
        private val getBudgetStatusUseCase: GetBudgetStatusUseCase,
        private val getExpenseStatsUseCase: GetExpenseStatsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState(isLoading = true))
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadData()
    }

    fun setBudget(amount: Double) {
        viewModelScope.launch {
            val currentMonth = java.time.YearMonth.now().toString()
            budgetRepository.setMonthlyBudget(currentMonth, amount)
            loadData() // Refresh data
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val startOfMonth = LocalDate.now().withDayOfMonth(1)
            val today = LocalDate.now()

            // Observe recent expenses
            // Observe budget status
            // Fetch top categories (for current month)

            combine(expenseRepository.getRecentExpenses(5), getBudgetStatusUseCase()) {
                    recentExpenses,
                    budget ->

                // Calculate stats on background thread if needed, but here simple fetch
                val breakdown = getExpenseStatsUseCase.getCategoryBreakdown(startOfMonth, today)
                val dailyAvg = getExpenseStatsUseCase.getDailyAverage(startOfMonth, today)

                HomeState(
                        budgetStatus = budget,
                        recentExpenses = recentExpenses,
                        topCategories = breakdown.take(3), // Top 3 categories
                        dailyAverage = dailyAvg,
                        isLoading = false
                )
            }
                    .collect { newState -> _state.value = newState }
        }
    }
}
