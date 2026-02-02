package com.aspharier.finora.ui.screens.exchange

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ExchangeState())
    val state: StateFlow<ExchangeState> = _state.asStateFlow()

    fun onSwap() {
        _state.value = _state.value.copy(
            fromCurrency = _state.value.toCurrency,
            toCurrency = _state.value.fromCurrency,
            fromAmount = _state.value.toAmount,
            toAmount = _state.value.fromAmount
        )
    }
}