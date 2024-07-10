package com.bmprj.weatherforecast.model

data class Tomorrow(
    val cityname:String?,
    val date:String?,
    val degree:String?,
    val conditionText:String?,
    val humidity:String?,
    val uv:String?,
    val totalPrecip:String?,
    val wind_kph:String?,
    val code : Int?
)
