package com.aspharier.finora.ui.screens.statistics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.aspharier.finora.domain.mapper.StatisticsMapper
import com.aspharier.finora.domain.usecase.GetStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getStatisticsUseCase: GetStatisticsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(StatisticsState())
    val state: StateFlow<StatisticsState> = _state.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun refresh(mode: StatsMode, range: TimeRange) {
        val result = getStatisticsUseCase.execute(mode, range)
        _state.value = StatisticsMapper.toUi(result)
    }

    fun toggleMode(mode: StatsMode) {
        _state.value = _state.value.copy(mode = mode)
    }

    fun changeRange(range: TimeRange) {
        _state.value = _state.value.copy(timeRange = range)
    }
}