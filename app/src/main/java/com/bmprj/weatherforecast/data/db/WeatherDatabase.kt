package com.bmprj.weatherforecast.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bmprj.weatherforecast.data.db.typeconverter.CurrentTypeConverter
import com.bmprj.weatherforecast.data.db.typeconverter.ForecastTypeConverter
import com.bmprj.weatherforecast.data.db.typeconverter.LocationTypeConverter
import com.bmprj.weatherforecast.data.model.Weather

@Database(entities = arrayOf(Weather::class), version = 1,exportSchema = false)
@TypeConverters(CurrentTypeConverter::class, ForecastTypeConverter::class, LocationTypeConverter::class)

abstract class WeatherDatabase : RoomDatabase() {
        abstract fun weatherDAO() :WeatherDAO


        //singleton

        companion object{

            //volatile her bir thread için sırayla nesneye erişmeyi sağlıyor.
            @Volatile private var instance :WeatherDatabase?=null

            private val lock = Any()

            operator fun invoke(context: Context) = instance ?: synchronized(lock){
                instance ?: makeDatabase(context).also {
                    instance = it
                }
            }

            private fun makeDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext, WeatherDatabase::class.java,"weatherdatabase"
            ).build()
        }

}