package com.bmprj.weatherforecast.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hour(
    @SerialName("chance_of_rain")
    val chance_of_rain: Int,
    @SerialName("condition")
    val condition: Condition,
    @SerialName("is_day")
    val is_day: Int,
    @SerialName("precip_mm")
    val precip_mm: Double,
    @SerialName("temp_c")
    val temp_c: Double,
    @SerialName("time")
    val time: String,
    @SerialName("wind_degree")
    val wind_degree: Int,
    @SerialName("wind_dir")
    val wind_dir: String,
    @SerialName("wind_kph")
    val wind_kph: Double
)