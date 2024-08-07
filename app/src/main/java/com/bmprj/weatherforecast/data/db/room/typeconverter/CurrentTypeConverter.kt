package com.bmprj.weatherforecast.data.db.room.typeconverter

import androidx.room.TypeConverter
import com.bmprj.weatherforecast.model.Current
import com.google.gson.Gson

class CurrentTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromCurrent(current: Current): String {
        return gson.toJson(current)
    }

    @TypeConverter
    fun toCurrent(currentJson: String): Current {
        return gson.fromJson(currentJson, Current::class.java)
    }
}