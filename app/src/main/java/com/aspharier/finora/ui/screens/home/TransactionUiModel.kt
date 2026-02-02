package com.aspharier.finora.ui.screens.home

data class TransactionUiModel(
    val id: String,
    val merchant: String,
    val category: String,
    val amount: Double,
    val isCredit: Boolean,
    val timestamp: String = ""
)
