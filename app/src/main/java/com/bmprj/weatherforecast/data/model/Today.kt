package com.bmprj.weatherforecast.data.model

import kotlinx.serialization.Serializable

data class Today(
    val cityname:String?,
    val date:String?,
    val degree:String?,
    val conditionText:String?,
    val humidity:String?,
    val uv:String?,
    val totalPrecip:String?,
    val wind_kph:String?,
    val wind_dir:Int,
    val wind_direction:String?
)
