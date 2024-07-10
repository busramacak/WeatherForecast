package com.bmprj.weatherforecast.data.db.room.repository

import com.bmprj.weatherforecast.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun insertAll(weather: Weather): Flow<Unit>
    suspend fun getWeather(): Flow<Weather>
    suspend fun delete(): Flow<Unit>
}