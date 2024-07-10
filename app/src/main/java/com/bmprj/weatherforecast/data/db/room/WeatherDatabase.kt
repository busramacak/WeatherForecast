package com.bmprj.weatherforecast.data.db.room


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bmprj.weatherforecast.data.db.room.typeconverter.CurrentTypeConverter
import com.bmprj.weatherforecast.data.db.room.typeconverter.ForecastTypeConverter
import com.bmprj.weatherforecast.data.db.room.typeconverter.LocationTypeConverter
import com.bmprj.weatherforecast.model.Search
import com.bmprj.weatherforecast.model.Weather

@Database(entities = [Weather::class, Search::class], version = 3)
@TypeConverters(CurrentTypeConverter::class, ForecastTypeConverter::class, LocationTypeConverter::class)
abstract class WeatherDatabase : RoomDatabase() {
        abstract fun weatherDAO() : WeatherDAO

}