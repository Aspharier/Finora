package com.aspharier.finora.data.remote.dto

import com.squareup.moshi.Json

data class ExchangeRateDto(
    @Json(name = "base_code")
    val baseCode: String,

    @Json(name = "conversion_rates")
    val rates: Map<String, Double>
)
