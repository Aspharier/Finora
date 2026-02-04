package com.aspharier.finora.di

import android.content.Context
import androidx.room.Room
import com.aspharier.finora.data.local.AppDatabase
import com.aspharier.finora.data.local.dao.BudgetDao
import com.aspharier.finora.data.local.dao.CategoryDao
import com.aspharier.finora.data.local.dao.ExchangeRateDao
import com.aspharier.finora.data.local.dao.ExpenseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "finora.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(appDatabase: AppDatabase): ExpenseDao {
        return appDatabase.expenseDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }

    @Provides
    @Singleton
    fun provideBudgetDao(appDatabase: AppDatabase): BudgetDao {
        return appDatabase.budgetDao()
    }

    @Provides
    @Singleton
    fun provideExchangeRateDao(appDatabase: AppDatabase): ExchangeRateDao {
        return appDatabase.exchangeRateDao()
    }
}