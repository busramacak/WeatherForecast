package com.bmprj.weatherforecast.data.remote

import com.bmprj.weatherforecast.data.model.Weather
import com.bmprj.weatherforecast.data.model.SearchCity
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface APIService {
    @GET("forecast.json")
    fun getWeather(
        @Query("key") key:String,
        @Query("q") q :String?,
        @Query("days") days:Int,
        @Query("aqi") aqi:String,
        @Query("lang") lang:String

    ): Call<Weather>

    @GET("search.json")
    fun getSearch(
        @Query("key") key:String ,
        @Query("q") q:String
    ) : Call<SearchCity>
}