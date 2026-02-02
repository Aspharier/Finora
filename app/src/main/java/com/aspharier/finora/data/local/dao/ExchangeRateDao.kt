package com.aspharier.finora.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aspharier.finora.data.local.entity.ExchangeRateEntity

@Dao
interface ExchangeRateDao {

    @Query("SELECT * FROM exchange_rates")
    suspend fun getAllRates(): List<ExchangeRateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<ExchangeRateEntity>)
}