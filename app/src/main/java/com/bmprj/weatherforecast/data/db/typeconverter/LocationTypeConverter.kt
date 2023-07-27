package com.bmprj.weatherforecast.data.db.typeconverter

import androidx.room.TypeConverter
import com.bmprj.weatherforecast.data.model.Location
import com.google.gson.Gson

class LocationTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromLocation(location: Location): String {
        return gson.toJson(location)
    }

    @TypeConverter
    fun toLocation(locationJson: String): Location {
        return gson.fromJson(locationJson, Location::class.java)
    }
}