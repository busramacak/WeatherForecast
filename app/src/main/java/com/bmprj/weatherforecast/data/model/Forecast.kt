package com.bmprj.weatherforecast.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
data class Forecast(
    @SerialName("forecastday")
    val forecastday: List<Forecastday>
)