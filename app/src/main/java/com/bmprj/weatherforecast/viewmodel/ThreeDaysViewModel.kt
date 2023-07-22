package com.bmprj.weatherforecast.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.model.ThreeDay
import com.bmprj.weatherforecast.data.model.Weather
import com.bmprj.weatherforecast.data.remote.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class ThreeDaysViewModel:ViewModel() {

    private val weatherApiUtils = ApiUtils()

    val threeDay = MutableLiveData<ArrayList<ThreeDay>>()


    fun refreshData(context: Context, key:String, q:String?, days:Int, aqi:String, lang:String){
        getDataFromApi(context, key, q, days, aqi, lang)
    }

    private fun getDataFromApi(context: Context, key:String, q:String?, days:Int, aqi:String, lang:String){

        val dayly = ArrayList<ThreeDay>()

        weatherApiUtils.getData(key, q, days, aqi, lang).enqueue(object : Callback<Weather>{
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

                val cityName = response.body()?.location?.name
                val forecastday = response.body()?.forecast?.forecastday

                for(i in 0 until forecastday!!.size){

                    val hour = forecastday[i]

                    val day = hour.day
                    val date = hour.date

                    val inFormat = SimpleDateFormat("yyyy-MM-dd")
                    val dat: Date = inFormat.parse(date) as Date
                    val outFormatDays = SimpleDateFormat("EEEE")
                    val goal: String = outFormatDays.format(dat)
                    val outFormatMonth = SimpleDateFormat("MMM")
                    val month:String = outFormatMonth.format(dat)
                    val outFormatDay = SimpleDateFormat("dd")
                    val dy : String = outFormatDay.format(dat)

                    val max_temp = day.maxtemp_c
                    val min_temp = day.mintemp_c

                    val condition =day.condition
                    val icon = condition.icon
                    val conditionText = condition.text

                    val t = ThreeDay(cityName,
                        context.getString(R.string.day_month_dy,goal,month,dy),
                        conditionText,
                        context.getString(R.string.degre,max_temp.toString()),
                        context.getString(R.string.degre, min_temp.toString()),icon)

                    dayly.add(t)
                }

                threeDay.value=dayly
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }
}