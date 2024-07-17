package com.bmprj.weatherforecast.data.db.room.repository

import com.bmprj.weatherforecast.data.db.room.WeatherDAO
import com.bmprj.weatherforecast.model.Search
import com.bmprj.weatherforecast.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl  @Inject constructor(
    private val weatherDAO: WeatherDAO
) : WeatherRepository {
    override suspend fun insertAll(weather: Weather): Flow<Unit> = flow {
        emit(weatherDAO.insertAll(weather))
    }

    override suspend fun getWeather(): Flow<Weather>  = flow {
        emit(weatherDAO.getWeather())
    }

    override suspend fun insertSearch(search: Search): Flow<Unit> = flow{
        emit(weatherDAO.insertSearch(search))
    }

    override suspend fun getSearch(id:Int): Flow<Search?> = flow {
        emit(weatherDAO.getSearch(id))
    }
}