package com.bmprj.weatherforecast

import retrofit2.create

class ApiUtils {

    companion object{

        val BASE_URL="https://api.weatherapi.com/v1/"


        fun getUrlInterface():APIService{

            return Client.getClient(BASE_URL).create(APIService::class.java)
        }
    }
}