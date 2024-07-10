package com.bmprj.weatherforecast.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.db.room.WeatherDatabase
import com.bmprj.weatherforecast.model.ThreeDay
import com.bmprj.weatherforecast.model.Weather
import com.bmprj.weatherforecast.base.BaseViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class ThreeDaysViewModel(application: Application): BaseViewModel(application){

    val threeDay = MutableLiveData<ArrayList<ThreeDay>>()



    fun refreshData(){

        getDataFromSQLite()
    }

    private fun getDataFromSQLite(){
        launch {
            val weathers = WeatherDatabase(getApplication()).weatherDAO().getWeather()
            showWeathers(weathers)
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun showWeathers(weather: Weather){
        val dayly = ArrayList<ThreeDay>()

        val cityName = weather.location.name
        val forecastday = weather.forecast.forecastday

        for(i in 0 until forecastday.size){

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
                getApplication<Application>().getString(R.string.day_month_dy,goal,month,dy),
                conditionText,
                getApplication<Application>().getString(R.string.degre,max_temp.toString()),
                getApplication<Application>().getString(R.string.degre, min_temp.toString()),icon)

            dayly.add(t)
        }

        threeDay.value=dayly


    }
}