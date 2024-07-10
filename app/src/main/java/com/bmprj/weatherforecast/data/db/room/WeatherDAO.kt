package com.bmprj.weatherforecast.data.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bmprj.weatherforecast.model.Weather

@Dao
interface WeatherDAO {

    @Insert
    suspend fun insertAll(weather: Weather)


    @Query("SELECT * FROM weather ")
    suspend fun getWeather(): Weather


    @Query("DELETE FROM weather")
    suspend fun delete()





}