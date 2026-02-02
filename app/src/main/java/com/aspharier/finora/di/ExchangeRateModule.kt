package com.aspharier.finora.di

import android.content.Context
import androidx.room.Room
import com.aspharier.finora.BuildConfig
import com.aspharier.finora.data.local.ExchangeRateDatabase
import com.aspharier.finora.data.local.dao.ExchangeRateDao
import com.aspharier.finora.data.remote.api.ExchangeRateApi
import com.aspharier.finora.data.remote.interceptor.RateLimitInterceptor
import com.aspharier.finora.data.repository.ExchangeRateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ExchangeRateModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient =
            OkHttpClient.Builder().addInterceptor(RateLimitInterceptor()).build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .baseUrl("https://v6.exchangerate-api.com/")
                    .client(okHttpClient)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): ExchangeRateApi =
            retrofit.create(ExchangeRateApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ExchangeRateDatabase =
            Room.databaseBuilder(context, ExchangeRateDatabase::class.java, "exchange_rate_db")
                    .build()

    @Provides fun provideDao(db: ExchangeRateDatabase): ExchangeRateDao = db.exchangeRateDao()

    @Provides
    @Singleton
    fun provideRepository(api: ExchangeRateApi, dao: ExchangeRateDao): ExchangeRateRepository =
            ExchangeRateRepository(api = api, dao = dao, apiKey = BuildConfig.EXCHANGE_RATE_API_KEY)
}
