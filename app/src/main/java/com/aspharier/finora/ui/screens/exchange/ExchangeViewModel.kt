package com.aspharier.finora.ui.screens.exchange

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspharier.finora.data.remote.interceptor.RateLimitException
import com.aspharier.finora.data.repository.ExchangeRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExchangeViewModel @Inject constructor(private val repository: ExchangeRateRepository) :
        ViewModel() {

    private val _state = MutableStateFlow(ExchangeState())
    val state: StateFlow<ExchangeState> = _state.asStateFlow()

    fun onKeyPress(key: String) {
        val current = _state.value.fromAmount
        val updated =
                when (key) {
                    "⌫" -> current.dropLast(1).ifEmpty { "0" }
                    "." -> if (current.contains(".")) current else "$current."
                    else -> if (current == "0") key else current + key
                }

        recalculate(updated)
    }

    fun onSwap() {
        _state.value =
                _state.value.copy(
                        fromCurrency = _state.value.toCurrency,
                        toCurrency = _state.value.fromCurrency,
                        fromAmount = _state.value.toAmount,
                        toAmount = _state.value.fromAmount
                )
    }

    fun refreshRates() {
        viewModelScope.launch {
            repository.getRates()
            _state.value = _state.value.copy(lastUpdated = "Live rate")
        }
    }

    fun refreshRatesSafely() {
        viewModelScope.launch {
            try {
                repository.getRates()
                _state.value = _state.value.copy(lastUpdated = "Live rate")
            } catch (e: RateLimitException) {
                _state.value = _state.value.copy(lastUpdated = "Using cached rate")
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun recalculate(amount: String) {
        val state = _state.value
        val converted =
                ExchangeCalculator.convert(
                        amount = amount,
                        fromCurrency = state.fromCurrency.code,
                        toCurrency = state.toCurrency.code
                )

        _state.value =
                state.copy(
                        fromAmount = amount,
                        toAmount = converted,
                        rateInfo =
                                "1 ${state.fromCurrency.code} ≈ ${
                String.format("%.2f",
                    LocalRates.rate(
                        state.fromCurrency.code,
                        state.toCurrency.code
                    )
                )
            } ${state.toCurrency.code}",
                        lastUpdated = "Local rate"
                )
    }
}
