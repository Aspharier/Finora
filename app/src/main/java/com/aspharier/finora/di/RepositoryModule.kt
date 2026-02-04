package com.aspharier.finora.di

import com.aspharier.finora.data.repository.RoomCategoryRepository
import com.aspharier.finora.domain.repository.CategoryRepository
import com.aspharier.finora.data.repository.RoomExpenseRepository
import com.aspharier.finora.domain.repository.ExpenseRepository
import com.aspharier.finora.data.repository.RoomBudgetRepository
import com.aspharier.finora.domain.repository.BudgetRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        roomCategoryRepository: RoomCategoryRepository
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        roomExpenseRepository: RoomExpenseRepository
    ): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        roomBudgetRepository: RoomBudgetRepository
    ): BudgetRepository
}