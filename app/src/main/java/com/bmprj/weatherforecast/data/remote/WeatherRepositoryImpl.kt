package com.bmprj.weatherforecast.data.remote

import com.bmprj.weatherforecast.data.db.room.WeatherDAO
import com.bmprj.weatherforecast.data.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl  @Inject constructor(
    private val weatherDAO: WeatherDAO
) : WeatherRepository{
    override suspend fun insertAll(weather: Weather): Flow<Unit> = flow {
        emit(weatherDAO.insertAll(weather))
    }

    override suspend fun getWeather(): Flow<Weather>  = flow {
        emit(weatherDAO.getWeather())
    }

    override suspend fun delete(): Flow<Unit> = flow  {
        emit(weatherDAO.delete())
    }
}