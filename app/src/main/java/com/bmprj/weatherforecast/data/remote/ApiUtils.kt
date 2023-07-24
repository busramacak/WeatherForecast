package com.bmprj.weatherforecast.data.remote

import android.content.Context
import com.bmprj.weatherforecast.data.model.SearchCity
import com.bmprj.weatherforecast.data.model.Weather
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
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