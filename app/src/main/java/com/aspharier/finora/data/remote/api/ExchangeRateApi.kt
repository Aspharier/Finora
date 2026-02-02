package com.aspharier.finora.data.remote.api

import com.aspharier.finora.data.remote.dto.ExchangeRateDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {

    @GET("v6/{apiKey}/latest/USD")
    suspend fun getLatestRates(
        @Path("apiKey") apiKey: String
    ): ExchangeRateDto
}