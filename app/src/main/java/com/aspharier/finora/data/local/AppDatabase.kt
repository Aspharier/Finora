package com.aspharier.finora.data.local

import androidx.room.RoomDatabase
import com.aspharier.finora.data.local.dao.ExchangeRateDao
import com.aspharier.finora.data.local.dao.TransactionDao

abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun exchangeRateDao(): ExchangeRateDao
}