package com.bmprj.weatherforecast.viewmodel

import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.db.WeatherDatabase
import com.bmprj.weatherforecast.data.model.Hourly
import com.bmprj.weatherforecast.data.model.Rainy
import com.bmprj.weatherforecast.data.model.Tomorrow
import com.bmprj.weatherforecast.data.model.Weather
import com.bmprj.weatherforecast.data.model.Wind
import com.bmprj.weatherforecast.data.remote.ApiUtils
import com.bmprj.weatherforecast.ui.base.BaseViewModel
import com.bmprj.weatherforecast.util.CustomSharedPreferences
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class TomorrowViewModel(application: Application):BaseViewModel(application) {
    private val weatherApiUtils = ApiUtils()


    val weathers = MutableLiveData<Weather>()
    val hourlyTom = MutableLiveData<ArrayList<Hourly>>()
    val rainyTom = MutableLiveData<ArrayList<Rainy>>()
    val windyTom = MutableLiveData<ArrayList<Wind>>()

    val tomorrow = MutableLiveData<Tomorrow>()

    private var customSharedPreferences = CustomSharedPreferences(getApplication())
    private val refreshTime = 15*60*1000*1000*1000L
    private val uid = 2

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshData( key:String, q:String?, days:Int, aqi:String, lang:String){
        val updateTime = customSharedPreferences.getTime()

        getDataFromSQLite()
//        if(updateTime!=null && updateTime!=0L && System.nanoTime() - updateTime < refreshTime){
//            getDataFromSQLite()
//        }else{
//            getDataFromApi(key, q, days, aqi, lang)
//
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDataFromSQLite(){
        launch {
            val weathers = WeatherDatabase(getApplication()).weatherDAO().getWeather()
            showWeathers(weathers)
            Toast.makeText(getApplication(),"countries From SQLite", Toast.LENGTH_LONG).show()

        }
    }
    private fun getDataFromApi( key:String, q:String?, days:Int, aqi:String, lang:String){


        weatherApiUtils.getData(key,q,days,aqi,lang).enqueue(object : Callback<Weather>{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

                weathers.value=response.body()
                val weather=Weather(weathers.value?.current!!,weathers.value?.forecast!!,weathers.value?.location!!)

                storeInSQLite(weather)
                Toast.makeText(getApplication(),"countries From API",Toast.LENGTH_LONG).show()


            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun storeInSQLite(weather: Weather) {
        launch {
            val dao = WeatherDatabase(getApplication()).weatherDAO()
            dao.delete()
            dao.insertAll(weather) //listeyi tekil eleman haline getirmeyi sağlıyor

            weather.uid=1
            showWeathers(weather)
        }

        customSharedPreferences.saveTime(System.nanoTime())
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


