package com.bmprj.weatherforecast.ui.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.db.room.WeatherDatabase
import com.bmprj.weatherforecast.model.Hourly
import com.bmprj.weatherforecast.model.Rainy
import com.bmprj.weatherforecast.model.Tomorrow
import com.bmprj.weatherforecast.model.Weather
import com.bmprj.weatherforecast.model.Wind
import com.bmprj.weatherforecast.base.BaseViewModel
import kotlinx.coroutines.launch

class TomorrowViewModel(application: Application): BaseViewModel(application) {


    val hourlyTom = MutableLiveData<ArrayList<Hourly>>()
    val rainyTom = MutableLiveData<ArrayList<Rainy>>()
    val windyTom = MutableLiveData<ArrayList<Wind>>()

    val tomorrow = MutableLiveData<Tomorrow>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshData(){

        getDataFromSQLite()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDataFromSQLite(){
        launch {
            val weathers = WeatherDatabase(getApplication()).weatherDAO().getWeather()
            showWeathers(weathers)
        }
    }

    private fun showWeathers(weather: Weather){

        val hourlyTomorrow= ArrayList<Hourly>()
        val rainyTomorrow = ArrayList<Rainy>()
        val windTomorrow= ArrayList<Wind>()



        val cityName = weather.location.name
        val forecastday = weather.forecast.forecastday.get(1)
        val date = forecastday.date
        val max_temp_c_tomorrow = forecastday.day.maxtemp_c.toString()
        val min_temp_c_tomorrow = forecastday.day.mintemp_c.toString()
        val conditionTextT = forecastday.day.condition.text
        val conditioncodeT = forecastday.day.condition.code
        val humidityT = forecastday.day.avghumidity.toString()
        val uvT = forecastday.day.uv.toString()
        val totalPrecipT = forecastday.day.totalprecip_mm.toString()


        val hourTomorrow = weather.forecast.forecastday.get(1).hour

        var maxWind=0.0
        var minWind=0.0


        for(i in 0 until hourTomorrow.size){

            val hourSet = hourTomorrow[i]

            val temp_hour = hourSet.temp_c
            val cond_hour = hourSet.condition
            val cond_icon = cond_hour.icon
            val rain = hourSet.chance_of_rain
            val precip_hour = hourSet.precip_mm
            val wind_degr_hour = hourSet.wind_degree
            val wind_kph_hour = hourSet.wind_kph

            if(minWind>wind_kph_hour) minWind=wind_kph_hour

            if(maxWind< wind_kph_hour) maxWind=wind_kph_hour

            val r = Rainy(
                "%$rain",
                getApplication<Application>().getString(R.string.time,i.toString()),
                precip_hour.toString(),
                precip_hour.toFloat())

            rainyTomorrow.add(r)

            val w = Wind(
                wind_kph_hour.toString(),
                wind_kph_hour.toInt()*3,
                wind_degr_hour.toFloat(),
                getApplication<Application>().getString(R.string.time,i.toString()))

            windTomorrow.add(w)

            val h = Hourly(
                cond_icon,
                getApplication<Application>().getString(R.string.time,i.toString()),
                getApplication<Application>().getString(R.string.degre, temp_hour.toString()))

            hourlyTomorrow.add(h)

        }

        val tom = Tomorrow(
            cityName,
            date,
            getApplication<Application>().getString(R.string.max_minTemp,max_temp_c_tomorrow,min_temp_c_tomorrow),
            conditionTextT,
            humidityT,
            uvT,
            getApplication<Application>().getString(R.string.totalPrecip,totalPrecipT),
            getApplication<Application>().getString(R.string.max_minWind,minWind.toInt().toString(),maxWind.toInt().toString()),
            conditioncodeT)

        tomorrow.value=tom

        hourlyTom.value = hourlyTomorrow
        rainyTom.value=rainyTomorrow
        windyTom.value=windTomorrow


    }
}


