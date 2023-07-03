package com.bmprj.weatherforecast.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Forecast(
    @SerialName("forecastday")
    val forecastday: List<Forecastday>
)