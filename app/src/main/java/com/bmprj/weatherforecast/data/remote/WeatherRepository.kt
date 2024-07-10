package com.bmprj.weatherforecast.data.remote

import androidx.room.Insert
import androidx.room.Query
import com.bmprj.weatherforecast.data.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun insertAll(weather: Weather): Flow<Unit>
    suspend fun getWeather(): Flow<Weather>
    suspend fun delete(): Flow<Unit>
}