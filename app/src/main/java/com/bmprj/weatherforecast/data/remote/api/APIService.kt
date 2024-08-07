package com.bmprj.weatherforecast.data.remote.api

import com.bmprj.weatherforecast.model.Weather
import com.bmprj.weatherforecast.model.SearchCity
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface APIService {
    @GET("forecast.json")
    suspend fun getWeather(
        @Query("key") key:String,
        @Query("q") q :String?,
        @Query("days") days:Int,
        @Query("aqi") aqi:String,
        @Query("lang") lang:String

    ): Response<Weather>

    @GET("search.json")
    suspend fun getSearch(
        @Query("key") key:String ,
        @Query("q") q:String
    ) : Response<SearchCity>
}