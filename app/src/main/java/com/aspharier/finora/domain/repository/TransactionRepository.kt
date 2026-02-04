package com.aspharier.finora.domain.repository

import com.aspharier.finora.domain.model.StatisticResult
import com.aspharier.finora.domain.model.TransactionType
import java.time.LocalDate

interface TransactionRepository {
    fun getStatistics(
        type: TransactionType,
        start: LocalDate,
        end: LocalDate
    ): StatisticResult
}