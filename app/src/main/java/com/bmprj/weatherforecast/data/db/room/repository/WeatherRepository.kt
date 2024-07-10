package com.bmprj.weatherforecast.data.db.room.repository

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bmprj.weatherforecast.model.Search
import com.bmprj.weatherforecast.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun insertAll(weather: Weather): Flow<Unit>
    suspend fun getWeather(): Flow<Weather>
    suspend fun delete(): Flow<Unit>
    suspend fun insertSearch(id:Int, search:String):Flow<Unit>
    suspend fun updateSearch(search: Search) : Flow<Unit>
    suspend fun getSearch(): Flow<ArrayList<Search>>
}