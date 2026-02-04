package com.aspharier.finora.di

import com.aspharier.finora.BuildConfig
import com.aspharier.finora.data.local.dao.ExchangeRateDao
import com.aspharier.finora.data.remote.api.ExchangeRateApi
import com.aspharier.finora.data.repository.ExchangeRateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideExchangeRateRepository(
        api: ExchangeRateApi,
        dao: ExchangeRateDao
    ): ExchangeRateRepository {
        return ExchangeRateRepository(
            api = api,
            dao = dao,
            apiKey = BuildConfig.EXCHANGE_RATE_API_KEY
        )
    }
}