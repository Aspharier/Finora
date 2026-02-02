package com.aspharier.finora.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aspharier.finora.data.local.dao.ExchangeRateDao
import com.aspharier.finora.data.local.entity.ExchangeRateEntity

@Database(
    entities = [ExchangeRateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ExchangeRateDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
}