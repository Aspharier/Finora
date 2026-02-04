package com.aspharier.finora.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.aspharier.finora.data.local.aggregate.BarAggregate
import com.aspharier.finora.data.local.aggregate.CategoryAggregate
import java.util.Locale

@Dao
interface TransactionDao {
    @Query("""
        SELECT SUM(amount)
        FROM transactions
        WHERE type = :type
        AND timestamp BETWEEN :start AND :end
        """)
    suspend fun totalAmount(
        type: String,
        start: Long,
        end: Long
    ): Double?

    @Query("""
        SELECT category AS label, SUM(amount) AS value
        FROM transactions
        WHERE type = :type
        AND timestamp BETWEEN :start AND :end
        GROUP BY category
    """)
    suspend fun categoryBreakdown(
        type: String,
        start: Long,
        end: Long
    ): List<CategoryAggregate>

    @Query("""
        SELECT strftime('%W', timestamp / 1000, 'unixepoch') AS label,
               SUM(amount) AS value
        FROM transactions
        WHERE type = :type
        AND timestamp BETWEEN :start AND :end
        GROUP BY label
    """)
    suspend fun weeklyBars(
        type: String,
        start: Long,
        end: Long
    ): List<BarAggregate>
}