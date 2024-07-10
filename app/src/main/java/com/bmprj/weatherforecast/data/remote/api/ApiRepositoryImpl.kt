package com.bmprj.weatherforecast.data.remote.api

import com.bmprj.weatherforecast.model.SearchCity
import com.bmprj.weatherforecast.model.Weather
import com.bmprj.weatherforecast.util.ApiResources
import com.bmprj.weatherforecast.util.handleResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val apiService: APIService
) : ApiRepository {
    override suspend fun getWeather(
        key: String,
        q: String?,
        days: Int,
        aqi: String,
        lang: String,
    ): Flow<ApiResources<Weather>> = flow {
        val response = apiService.getWeather(key, q, days, aqi, lang)
        val isSuccessful = response.isSuccessful
        val result = handleResponse(isSuccessful,response)
        emit(result)
    }

    override suspend fun getSearch(key: String, q: String): Flow<ApiResources<SearchCity>> = flow {
        val response = apiService.getSearch(key, q)
        val isSuccessful = response.isSuccessful
        val result = handleResponse(isSuccessful,response)
        emit(result)
    }
}