package com.bmprj.weatherforecast.di

import com.bmprj.weatherforecast.model.SearchCity
import com.bmprj.weatherforecast.model.Weather
import com.bmprj.weatherforecast.data.remote.APIService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiUtils {

    companion object{

        private val BASE_URL="https://api.weatherapi.com/v1/"

        private val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)
    }

    fun getData( key:String, q:String?, days:Int, aqi:String, lang:String): Call<Weather> {
        return api.getWeather(key,q,days,aqi,lang)
    }

    fun getSearch(key:String, query:String): Call<SearchCity>{
        return api.getSearch(key,query)
    }
}