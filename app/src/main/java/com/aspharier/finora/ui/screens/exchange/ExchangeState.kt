package com.aspharier.finora.ui.screens.exchange

data class CurrencyUiModel(
    val code: String,
    val name: String,
    val symbol: String,
    val flag: String
)

data class ExchangeState(
    val fromCurrency: CurrencyUiModel = CurrencyUiModel("USD", "US Dollar", "$", "🇺🇸"),
    val toCurrency: CurrencyUiModel = CurrencyUiModel("INR", "Indian Rupee", "₹", "🇮🇳"),
    val fromAmount: String = "100",
    val toAmount: String = "8320.50",
    val rateInfo: String = "1 USD ≈ 83.20 INR",
    val lastUpdated: String = "Updated just now"
)