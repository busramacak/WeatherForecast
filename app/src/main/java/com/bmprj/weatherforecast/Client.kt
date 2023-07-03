package com.bmprj.weatherforecast

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client {

    companion object{

        fun getClient(BASE_URL:String): Retrofit {

                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

        }
    }


}