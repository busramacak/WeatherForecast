package com.bmprj.weatherforecast.ui.threeDays

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.viewModelScope
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.model.ThreeDay
import com.bmprj.weatherforecast.model.Weather
import com.bmprj.weatherforecast.base.BaseViewModel
import com.bmprj.weatherforecast.data.db.room.repository.WeatherRepositoryImpl
import com.bmprj.weatherforecast.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ThreeDaysViewModel @Inject constructor(
    application: Application,
    private val weatherRepository:WeatherRepositoryImpl,
    ): BaseViewModel(application){

    private val _threeDay = MutableStateFlow<UiState<List<ThreeDay>>>(UiState.Loading)
    val threeDay get() = _threeDay.asStateFlow()


    fun refreshData(){
        getDataFromSQLite()
    }

    private fun getDataFromSQLite() = viewModelScope.launch{
        weatherRepository.getWeather().collect{
            showWeathers(it)
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun showWeathers(weather: Weather) = viewModelScope.launch{
        val dayly = ArrayList<ThreeDay>()

        val cityName = weather.location.name
        val forecastDay = weather.forecast.forecastday

        for(element in forecastDay){

            val hour = element

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

        _threeDay.emit(UiState.Success(dayly))
    }
}