package com.bmprj.weatherforecast.ui.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.db.room.WeatherDatabase
import com.bmprj.weatherforecast.model.Hourly
import com.bmprj.weatherforecast.model.Rainy
import com.bmprj.weatherforecast.model.Tomorrow
import com.bmprj.weatherforecast.model.Weather
import com.bmprj.weatherforecast.model.Wind
import com.bmprj.weatherforecast.base.BaseViewModel
import com.bmprj.weatherforecast.data.db.room.repository.WeatherRepositoryImpl
import com.bmprj.weatherforecast.model.Today
import com.bmprj.weatherforecast.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TomorrowViewModel @Inject constructor(
    application: Application,
    private val weatherRepository:WeatherRepositoryImpl
): BaseViewModel(application) {


    private val _hourlyTom = MutableStateFlow<UiState<List<Hourly>>>(UiState.Loading)
    val hourlyTom get() = _hourlyTom.asStateFlow()

    private val _rainyTom = MutableStateFlow<UiState<List<Rainy>>>(UiState.Loading)
    val rainyTom get() = _rainyTom.asStateFlow()

    private val _windyTom = MutableStateFlow<UiState<List<Wind>>>(UiState.Loading)
    val windyTom get() = _windyTom.asStateFlow()

    private val _tomorrow = MutableStateFlow<UiState<Tomorrow>>(UiState.Loading)
    val tomorrow get() = _tomorrow.asStateFlow()


    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshData(){
        getDataFromSQLite()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDataFromSQLite() = viewModelScope.launch{
            weatherRepository.getWeather().collect{
                showWeathers(it)
            }
    }

    private fun showWeathers(weather: Weather) = viewModelScope.launch{

        val hourlyTomorrow= ArrayList<Hourly>()
        val rainyTomorrow = ArrayList<Rainy>()
        val windTomorrow= ArrayList<Wind>()



        val cityName = weather.location.name
        val forecastday = weather.forecast.forecastday[1]
        val date = forecastday.date
        val max_temp_c_tomorrow = forecastday.day.maxtemp_c.toString()
        val min_temp_c_tomorrow = forecastday.day.mintemp_c.toString()
        val conditionTextT = forecastday.day.condition.text
        val conditioncodeT = forecastday.day.condition.code
        val humidityT = forecastday.day.avghumidity.toString()
        val uvT = forecastday.day.uv.toString()
        val totalPrecipT = forecastday.day.totalprecip_mm.toString()


        val hourTomorrow = weather.forecast.forecastday[1].hour

        var maxWind=0.0
        var minWind=0.0


        for(i in hourTomorrow.indices){

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

        _tomorrow.emit(UiState.Success(tom))

        _hourlyTom.emit(UiState.Success(hourlyTomorrow))
        _rainyTom.emit(UiState.Success(rainyTomorrow))
        _windyTom.emit(UiState.Success(windTomorrow))


    }
}


