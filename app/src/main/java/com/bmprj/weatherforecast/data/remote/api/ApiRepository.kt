package com.bmprj.weatherforecast.data.remote.api

import com.bmprj.weatherforecast.model.SearchCity
import com.bmprj.weatherforecast.model.Weather
import com.bmprj.weatherforecast.util.ApiResources
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Query

interface ApiRepository {
    suspend fun getWeather(key:String, q :String?, days:Int, aqi:String, lang:String) : Flow<ApiResources<Weather>>
    suspend fun getSearch(key:String, q:String) : Flow<ApiResources<SearchCity>>
}