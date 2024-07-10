package com.bmprj.weatherforecast.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
data class Current(
    @SerialName("condition")
    val condition: Condition,
    @SerialName("is_day")
    val is_day: Int,
    @SerialName("last_updated")
    val last_updated: String,
    @SerialName("pressure_mb")
    val pressure_mb: Double,
    @SerialName("temp_c")
    val temp_c: Double,
    @SerialName("uv")
    val uv: Double,
    @SerialName("wind_degree")
    val wind_degree: Int,
    @SerialName("wind_kph")
    val wind_kph: Double
)