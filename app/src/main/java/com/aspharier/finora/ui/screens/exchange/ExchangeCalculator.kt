package com.aspharier.finora.ui.screens.exchange

object ExchangeCalculator {

    fun convert(
        amount: String,
        fromCurrency: String,
        toCurrency: String
    ) : String {
        val value = amount.toDoubleOrNull() ?: return "0.00"
        val rate = LocalRates.rate(fromCurrency, toCurrency)
        val result = value * rate
        return format(result)
    }
    private fun format(value: Double): String {
        return String.format("%.2f", value)
    }
}