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

    override suspend fun delete(): Flow<Unit> = flow  {
        emit(weatherDAO.delete())
    }

    override suspend fun insertSearch(id: Int, search: String): Flow<Unit> = flow{
        emit(weatherDAO.insertSearch(id, search))
    }

    override suspend fun updateSearch(search: Search): Flow<Unit> = flow {
        emit(weatherDAO.updateSearch(search))
    }

    override suspend fun getSearch(): Flow<ArrayList<Search>> = flow {
        emit(weatherDAO.getSearch())
    }
}