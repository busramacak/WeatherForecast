package com.bmprj.weatherforecast.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.db.sqlite.DAO
import com.bmprj.weatherforecast.data.db.sqlite.DatabaseHelper
import com.bmprj.weatherforecast.data.db.room.WeatherDatabase
import com.bmprj.weatherforecast.model.Hourly
import com.bmprj.weatherforecast.model.Rainy
import com.bmprj.weatherforecast.model.Today
import com.bmprj.weatherforecast.model.Weather
import com.bmprj.weatherforecast.model.Wind
import com.bmprj.weatherforecast.di.ApiUtils
import com.bmprj.weatherforecast.base.BaseViewModel
import com.bmprj.weatherforecast.util.CustomSharedPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TodayViewModel(application: Application): BaseViewModel(application) {
    private val weatherApiUtils = ApiUtils()

    private var lastCity = MutableLiveData<String>()



    val weathers = MutableLiveData<Weather>()
    val hourlyTod = MutableLiveData<ArrayList<Hourly>>()
    val rainyTod = MutableLiveData<ArrayList<Rainy>>()
    val windyTod = MutableLiveData<ArrayList<Wind>>()
    val today = MutableLiveData<Today>()

    private var customSharedPreferences = CustomSharedPreferences(getApplication())
    private val refreshTime = 15*60*1000*1000*1000L
    private val uid = 1





    val weatherError = MutableLiveData<Boolean>()
    val weatherLoading = MutableLiveData<Boolean>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshData(key: String, q: String?, days: Int, aqi: String, lang: String){

        val updateTime = customSharedPreferences.getTime()
        if(lastCity.value!=q){
            getDataFromApi(key, q, days, aqi, lang)
        }else{
            if(updateTime!=null && updateTime!=0L && System.nanoTime() - updateTime < refreshTime){
                getDataFromSQLite()
            }else{
                getDataFromApi(key, q, days, aqi, lang)

            }
        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDataFromSQLite(){
        launch {
            val weathers = WeatherDatabase(getApplication()).weatherDAO().getWeather()
            showWeathers(weathers)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDataFromApi(key: String, q: String?, days: Int, aqi: String, lang: String) {
        weatherLoading.value = true


        weatherApiUtils.getData(key, q, days, aqi, lang)
            .enqueue(object : Callback<Weather> {
                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(
                    call: Call<Weather>,
                    response: Response<Weather>,
                ) {
                    weathers.value = response.body()
                    val weather=
                        Weather(weathers.value?.current!!,weathers.value?.forecast!!,weathers.value?.location!!)

                    storeInSQLite(weather)

                }

                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    weatherLoading.value=false
                    weatherError.value=true
                    t.printStackTrace()
                }

            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun storeInSQLite(weather: Weather) {
        launch {
            val dao = WeatherDatabase(getApplication()).weatherDAO()
            dao.delete()
            dao.insertAll(weather)

            lastCity.value=weather.location.name
            weather.uid=uid
            showWeathers(weather)
        }

        customSharedPreferences.saveTime(System.nanoTime())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showWeathers(weather: Weather){
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
        val dh = DatabaseHelper(getApplication())

        if(DAO().get(dh).size==0){
            DAO().add(dh,1,cityName)

        }else
        {
            DAO().update(dh,1,cityName)
        }

        val current = weather.current
        val last_updated = weather.current.last_updated
        val temp_c = current.temp_c.toInt().toString()

        val condition = current.condition
        val condition_text = condition.text
        val condition_code = condition.code

        val day = weather.forecast.forecastday.get(0).day
        val humidity = day.avghumidity.toString()
        val uv = day.uv.toString()
        val totalPrecip = day.totalprecip_mm.toString()




        val hourToday = weather.forecast.forecastday.get(0).hour


        if(a<17){
            for(i in a until hourToday.size){
                val hourSet = hourToday.get(i)
                val hf = hourToday.get(a)
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
                val hourSet = hourToday.get(i)
                val hf = hourToday.get(a)
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


        today.value = tod


        hourlyTod.value = hourlyToday
        rainyTod.value = rainyToday
        windyTod.value = windToday

        weatherError.value = false
        weatherLoading.value = false
    }
}


