package com.bmprj.weatherforecast.data.db.room.typeconverter

import androidx.room.TypeConverter
import com.bmprj.weatherforecast.model.Forecast
import com.google.gson.Gson

class ForecastTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromForecast(forecast: Forecast): String {
        return gson.toJson(forecast)
    }

    @TypeConverter
    fun toForecast(forecastJson: String): Forecast {
        return gson.fromJson(forecastJson, Forecast::class.java)
    }
}