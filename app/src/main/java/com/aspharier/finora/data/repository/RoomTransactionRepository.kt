package com.aspharier.finora.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.aspharier.finora.data.local.dao.TransactionDao
import com.aspharier.finora.domain.model.StatisticResult
import com.aspharier.finora.domain.model.TransactionType
import com.aspharier.finora.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId

class RoomTransactionRepository(
    private val dao: TransactionDao
) : TransactionRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getStatistics(
        type: TransactionType,
        start: LocalDate,
        end: LocalDate
    ): StatisticResult = runBlocking {
        withContext(Dispatchers.IO) {
            val startMillis = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endMillis = end.plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli()

            val total = dao.totalAmount(type.name, startMillis, endMillis) ?: 0.0
            val categories = dao.categoryBreakdown(
                type.name,
                startMillis,
                endMillis
            ).associate {
                it.label to it.value
            }

            val bars = dao.weeklyBars(
                type.name,
                startMillis,
                endMillis
            ).map {
                it.label to it.value
            }

            StatisticResult(
                totalAmount = total,
                isPositiveTrend = total >= 0,
                breakdown = categories,
                bars = bars
            )
        }
    }
}
