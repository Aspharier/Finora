package com.aspharier.finora.ui.screens.exchange

object LocalRates {
    private val ratesFromUsd = mapOf(
        "USD" to 1.0,
        "INR" to 83.20,
        "EUR" to 0.92,
        "GBP" to 0.79,
        "JPY" to 148.10
    )

    fun rate(from: String, to: String): Double {
        val fromRate = ratesFromUsd[from] ?: return 1.0
        val toRate = ratesFromUsd[to] ?: return 1.0
        return toRate / fromRate
    }
}