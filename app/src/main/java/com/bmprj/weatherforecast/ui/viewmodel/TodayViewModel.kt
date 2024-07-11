package com.bmprj.weatherforecast.ui.viewmodel

import android.app.Application
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.model.Hourly
import com.bmprj.weatherforecast.model.Rainy
import com.bmprj.weatherforecast.model.Today
import com.bmprj.weatherforecast.model.Weather
import com.bmprj.weatherforecast.model.Wind
import com.bmprj.weatherforecast.base.BaseViewModel
import com.bmprj.weatherforecast.data.db.room.repository.WeatherRepositoryImpl
import com.bmprj.weatherforecast.data.remote.api.ApiRepositoryImpl
import com.bmprj.weatherforecast.data.remote.location.LocationRepositoryImpl
import com.bmprj.weatherforecast.model.Search
import com.bmprj.weatherforecast.util.ApiResources
import com.bmprj.weatherforecast.util.CustomSharedPreferences
import com.bmprj.weatherforecast.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    application: Application,
    private val apiRepository: ApiRepositoryImpl,
    private val weatherRepository:WeatherRepositoryImpl,
    private val locationRepository:LocationRepositoryImpl
): BaseViewModel(application) {

    private val lastCity = MutableLiveData<String>()

    private val _weathers = MutableStateFlow<UiState<Weather>>(UiState.Loading)

    private val _hourlyTod = MutableStateFlow<UiState<List<Hourly>>>(UiState.Loading)
    val hourlyTod get() = _hourlyTod.asStateFlow()

    private val _rainyTod = MutableStateFlow<UiState<List<Rainy>>>(UiState.Loading)
    val rainyTod get() = _rainyTod.asStateFlow()

    private val _windyTod = MutableStateFlow<UiState<List<Wind>>>(UiState.Loading)
    val windyTod get() = _windyTod.asStateFlow()

    private val _today = MutableStateFlow<UiState<Today>>(UiState.Loading)
    val today get() = _today.asStateFlow()

    private val _search = MutableStateFlow<UiState<List<Search>>>(UiState.Loading)
    val search get() = _search.asStateFlow()

    private val _location = MutableStateFlow<UiState<Location?>>(UiState.Loading)
    val location get() = _location.asStateFlow()

    private var customSharedPreferences = CustomSharedPreferences(getApplication())
    private val refreshTime = 15*60*1000*1000*1000L
    private val uid = 1

    lateinit var searchList:ArrayList<Search>


    fun getLocation() = viewModelScope.launch{
        locationRepository.getLocation().collect{
            _location.emit(UiState.Success(it))
        }
    }
    fun getSearch() = viewModelScope.launch {
        weatherRepository.getSearch().collect{
            _search.emit(UiState.Success(it))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshData(key: String, q: String?, days: Int, aqi: String, lang: String) = viewModelScope.launch{
        val updateTime = customSharedPreferences.getTime()
        if(lastCity.value!=q){
            getDataFromApi(key, q, days, aqi, lang)
        }else{
            if(updateTime!=null && updateTime!=0L && System.nanoTime() - updateTime < refreshTime){
                getDataFromDatabase()
            }else{
                getDataFromApi(key, q, days, aqi, lang)

            }
        }

    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDataFromDatabase() = viewModelScope.launch{
        weatherRepository.getWeather().collect{
            showWeathers(it)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getDataFromApi(key: String, q: String?, days: Int, aqi: String, lang: String) = viewModelScope.launch {
        apiRepository.getWeather(key, q, days, aqi, lang)
            .onStart { _weathers.emit(UiState.Loading) }
            .catch { _weathers.emit(UiState.Error(it)) }
            .collect{
                when(it){
                    is ApiResources.Failure -> _weathers.emit(UiState.Error(Throwable(it.exception.errorMessage)))
                    is ApiResources.Success -> {
                        _weathers.emit(UiState.Success(it.result))
                        storeInSQLite(it.result)
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun storeInSQLite(weather: Weather) = viewModelScope.launch{
        weatherRepository.delete()
            .collect{
                weatherRepository.insertAll(weather)
                    .collect{
                        println("oldiiiiiiiiiiiiiii")
                    }
            }


        lastCity.value=weather.location.name
        weather.uid=uid
        showWeathers(weather)

        customSharedPreferences.saveTime(System.nanoTime())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showWeathers(weather: Weather) = viewModelScope.launch{
        val hourlyToday = ArrayList<Hourly>()
        val rainyToday = ArrayList<Rainy>()
        val windToday = ArrayList<Wind>()




        val formatter = DateTimeFormatter.ofPattern("HH")
        val currentt = LocalDateTime.now().format(formatter)
        val a = currentt.toInt()

        var wind_kp=""
        var wind_degree = 0.0f
        var windDirection=""


        val cityName = weather.location.name

        weatherRepository.getSearch().collect{
            searchList = arrayListOf(it[0])
        }

        if(::searchList.isInitialized && searchList.isEmpty()){
            weatherRepository.insertSearch(Search(1,cityName))

        }else {
            weatherRepository.updateSearch(searchList[0])
        }


        val current = weather.current
        val last_updated = weather.current.last_updated
        val temp_c = current.temp_c.toInt().toString()

        val condition = current.condition
        val condition_text = condition.text
        val condition_code = condition.code

        val day = weather.forecast.forecastday[0].day
        val humidity = day.avghumidity.toString()
        val uv = day.uv.toString()
        val totalPrecip = day.totalprecip_mm.toString()




        val hourToday = weather.forecast.forecastday[0].hour


        if(a<17){
            for(i in a until hourToday.size){
                val hourSet = hourToday[i]
                val hf = hourToday[a]
                wind_kp = hf.wind_kph.toString()
                wind_degree = hf.wind_degree.toFloat()
                windDirection = hf.wind_dir

                val temp_hour = hourSet.temp_c
                val cond_hour = hourSet.condition
                val cond_icon = cond_hour.icon
                val rain = hourSet.chance_of_rain
                val precip_hour = hourSet.precip_mm
                val wind_degr_hour = hourSet.wind_degree
                val wind_kph_hour = hourSet.wind_kph

                val r = Rainy(
                    "%$rain",
                    getApplication<Application>().getString(R.string.time, i.toString()),
                    precip_hour.toString(),
                    precip_hour.toFloat()
                )
                rainyToday.add(r)
                val w = Wind(
                    wind_kph_hour.toString(),
                    wind_kph_hour.toInt() * 3,
                    wind_degr_hour.toFloat(),
                    getApplication<Application>().getString(R.string.time, i.toString())
                )
                windToday.add(w)
                val h = Hourly(
                    cond_icon,
                    getApplication<Application>().getString(R.string.time, i.toString()),
                    getApplication<Application>().getString(R.string.degre, temp_hour.toString())
                )
                hourlyToday.add(h)

            }
        }else{

            for (i in 17 until hourToday.size) {
                val hourSet = hourToday[i]
                val hf = hourToday[a]
                wind_kp = hf.wind_kph.toString()
                wind_degree = hf.wind_degree.toFloat()
                windDirection = hf.wind_dir

                val temp_hour = hourSet.temp_c
                val cond_hour = hourSet.condition
                val cond_icon = cond_hour.icon
                val rain = hourSet.chance_of_rain
                val precip_hour = hourSet.precip_mm
                val wind_degr_hour = hourSet.wind_degree
                val wind_kph_hour = hourSet.wind_kph

                val r = Rainy(
                    "%$rain",
                    getApplication<Application>().getString(R.string.time, i.toString()),
                    precip_hour.toString(),
                    precip_hour.toFloat()
                )
                rainyToday.add(r)
                val w = Wind(
                    wind_kph_hour.toString(),
                    wind_kph_hour.toInt() * 3,
                    wind_degr_hour.toFloat(),
                    getApplication<Application>().getString(R.string.time, i.toString())
                )
                windToday.add(w)
                val h = Hourly(
                    cond_icon,
                    getApplication<Application>().getString(R.string.time, i.toString()),
                    getApplication<Application>().getString(R.string.degre, temp_hour.toString())
                )
                hourlyToday.add(h)
            }

        }


        val tod = Today(
            cityName,
            last_updated,
            getApplication<Application>().getString(R.string.degre,temp_c),
            condition_text,
            humidity,
            uv,
            getApplication<Application>().getString(R.string.totalPrecip,totalPrecip),
            wind_kp,
            wind_degree,
            windDirection,
            condition_code)


        _today.emit(UiState.Success(tod))
        _hourlyTod.emit(UiState.Success(hourlyToday))
        _rainyTod.emit(UiState.Success(rainyToday))
        _windyTod.emit(UiState.Success(windToday))
    }
}


