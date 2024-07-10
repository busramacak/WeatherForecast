package com.bmprj.weatherforecast.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
data class SearchCityItem(
    @SerialName("country")
    val country: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("lat")
    val lat: Double,
    @SerialName("lon")
    val lon: Double,
    @SerialName("name")
    val name: String?,
    @SerialName("region")
    val region: String,
    @SerialName("url")
    val url: String
)