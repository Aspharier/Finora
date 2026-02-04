package com.aspharier.finora.domain.model

import java.time.LocalDate

enum class TransactionType {
    INCOME,
    EXPENSE
}

data class Transaction (
    val id: String,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val date: LocalDate
)