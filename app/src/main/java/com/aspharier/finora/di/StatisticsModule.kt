package com.aspharier.finora.di

import com.aspharier.finora.data.local.AppDatabase
import com.aspharier.finora.data.repository.FakeTransactionRepository
import com.aspharier.finora.data.repository.RoomTransactionRepository
import com.aspharier.finora.domain.repository.TransactionRepository
import com.aspharier.finora.domain.usecase.GetStatisticsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object StatisticsModule {
    @Provides
    fun provideTransactionRepository(
        db: AppDatabase
    ): TransactionRepository =
        RoomTransactionRepository(db.transactionDao())

    @Provides
    fun provideGetStatisticsUseCase(
        repository: TransactionRepository
    ): GetStatisticsUseCase =
        GetStatisticsUseCase(repository)
}