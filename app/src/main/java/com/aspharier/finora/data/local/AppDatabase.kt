package com.aspharier.finora.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aspharier.finora.data.local.dao.BudgetDao
import com.aspharier.finora.data.local.dao.CategoryDao
import com.aspharier.finora.data.local.dao.ExchangeRateDao
import com.aspharier.finora.data.local.dao.ExpenseDao
import com.aspharier.finora.data.local.entity.CategoryEntity
import com.aspharier.finora.data.local.entity.ExchangeRateEntity
import com.aspharier.finora.data.local.entity.ExpenseEntity
import com.aspharier.finora.data.local.entity.MonthlyBudgetEntity

@Database(
        entities =
                [
                        ExpenseEntity::class,
                        CategoryEntity::class,
                        MonthlyBudgetEntity::class,
                        ExchangeRateEntity::class],
        version = 5,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
        abstract fun expenseDao(): ExpenseDao
        abstract fun categoryDao(): CategoryDao
        abstract fun budgetDao(): BudgetDao
        abstract fun exchangeRateDao(): ExchangeRateDao
}
