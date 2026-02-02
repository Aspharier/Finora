package com.aspharier.finora.data.repository

import com.aspharier.finora.data.local.dao.ExchangeRateDao
import com.aspharier.finora.data.local.entity.ExchangeRateEntity
import com.aspharier.finora.data.remote.api.ExchangeRateApi
import java.lang.Exception
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExchangeRateRepository(
        private val api: ExchangeRateApi,
        private val dao: ExchangeRateDao,
        private val apiKey: String
) {
    private var lastFetchTime: Long = 0
    private val minIntervalMs = 60_000L

    suspend fun getRates(): Map<String, Double> =
            withContext(Dispatchers.IO) {
                val now = System.currentTimeMillis()

                // Use cache if within rate limit window
                if (now - lastFetchTime < minIntervalMs) {
                    val cached = dao.getAllRates()
                    if (cached.isNotEmpty()) {
                        return@withContext cached.associate { it.currencyCode to it.rate }
                    }
                }

                try {
                    val response = api.getLatestRates(apiKey)

                    val entities =
                            response.rates.map {
                                ExchangeRateEntity(
                                        currencyCode = it.key,
                                        rate = it.value,
                                        timestamp = now
                                )
                            }

                    dao.insertRates(entities)
                    lastFetchTime = now
                    response.rates
                } catch (e: Exception) {
                    val cached = dao.getAllRates()
                    if (cached.isNotEmpty()) {
                        cached.associate { it.currencyCode to it.rate }
                    } else {
                        localRateFallback()
                    }
                }
            }

    private fun localRateFallback(): Map<String, Double> =
            mapOf("USD" to 1.0, "INR" to 83.20, "EUR" to 0.92, "GBP" to 0.79, "JPY" to 148.10)
}
