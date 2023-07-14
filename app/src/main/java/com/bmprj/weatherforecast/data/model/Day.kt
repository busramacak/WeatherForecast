package com.bmprj.weatherforecast.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
data class Day(
    @SerialName("avghumidity")
    val avghumidity: Double,
    @SerialName("condition")
    val condition: Condition,
    @SerialName("daily_chance_of_rain")
    val daily_chance_of_rain: Int,
    @SerialName("maxtemp_c")
    val maxtemp_c: Double,
    @SerialName("mintemp_c")
    val mintemp_c: Double,
    @SerialName("totalprecip_mm")
    val totalprecip_mm: Double,
    @SerialName("totalsnow_cm")
    val totalsnow_cm: Double,
    @SerialName("uv")
    val uv: Double
)