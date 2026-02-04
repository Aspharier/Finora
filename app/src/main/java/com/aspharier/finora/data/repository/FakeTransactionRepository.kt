package com.aspharier.finora.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.aspharier.finora.domain.model.StatisticResult
import com.aspharier.finora.domain.model.Transaction
import com.aspharier.finora.domain.model.TransactionType
import com.aspharier.finora.domain.repository.TransactionRepository
import java.time.LocalDate

class FakeTransactionRepository : TransactionRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTransactions(): List<Transaction> {
        return listOf(
            Transaction("1", 6200.0, TransactionType.EXPENSE, "Shopping", LocalDate.now()),
            Transaction("2", 4100.0, TransactionType.EXPENSE, "Food", LocalDate.now()),
            Transaction("3", 2300.0, TransactionType.EXPENSE, "Transport", LocalDate.now()),
            Transaction("4", 45000.0, TransactionType.INCOME, "Salary", LocalDate.now())
        )
    }

    override fun getStatistics(
        type: TransactionType,
        start: LocalDate,
        end: LocalDate
    ): StatisticResult {
        TODO("Not yet implemented")
    }
}