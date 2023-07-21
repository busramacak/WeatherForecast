package com.bmprj.weatherforecast.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.model.Hourly
import com.bmprj.weatherforecast.data.model.Rainy
import com.bmprj.weatherforecast.data.model.Today
import com.bmprj.weatherforecast.data.model.Weather
import com.bmprj.weatherforecast.data.model.Wind
import com.bmprj.weatherforecast.data.remote.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TodayViewModel: ViewModel() {
    private val weatherApiUtils = ApiUtils()



    val weathers = MutableLiveData<Weather>()
    val hourly = MutableLiveData<ArrayList<Hourly>>()
    val rainy = MutableLiveData<ArrayList<Rainy>>()
    val windy = MutableLiveData<ArrayList<Wind>>()
    val today = MutableLiveData<Today>()

    val weatherError = MutableLiveData<Boolean>()
    val weatherLoading = MutableLiveData<Boolean>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshData(context: Context, key:String, q:String?, days:Int, aqi:String, lang:String){
        getDataFromApi(context,key,q,days,aqi,lang)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDataFromApi(context:Context, key:String, q:String?, days:Int, aqi:String, lang:String) {
        weatherLoading.value = true


        val hourl = ArrayList<Hourly>()
        val rainn = ArrayList<Rainy>()
        val wind = ArrayList<Wind>()


        val formatter = DateTimeFormatter.ofPattern("HH")
        val currentt = LocalDateTime.now().format(formatter)
        val a = currentt.toInt()

        var wind_kp=""
        var wind_degree = 0
        var windDirection=""


        weatherApiUtils.getData(key, q, days, aqi, lang)
            .enqueue(object : Callback<Weather> {
                override fun onResponse(
                    call: Call<Weather>,
                    response: Response<Weather>,
                ) {
                    weathers.value = response.body()

                    val cityName = response.body()?.location?.name
                    val current = response.body()?.current
                    val last_updated = response.body()?.current?.last_updated
                    val temp_c = current?.temp_c.toString()

                    val condition = current?.condition
                    val condition_text = condition?.text
                    val condition_code = condition?.code

                    val day = response.body()?.forecast?.forecastday?.get(0)?.day
                    val humidity = day?.avghumidity.toString()
                    val uv = day?.uv.toString()
                    val totalPrecip = day?.totalprecip_mm.toString()



                    val hour = response.body()?.forecast?.forecastday?.get(0)?.hour
                    for (i in 0 until hour?.size!!) {
                        val hourSet = hour.get(i)
                            val hf = hour.get(a)
                            wind_kp = hf.wind_kph.toString()
                            wind_degree = hf.wind_degree
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
                            context.getString(R.string.time, i.toString()),
                            precip_hour.toString(),
                            precip_hour.toFloat()
                        )
                        rainn.add(r)
                        val w = Wind(
                            wind_kph_hour.toString(),
                            wind_kph_hour.toInt() * 3,
                            wind_degr_hour.toFloat(),
                            context.getString(R.string.time, i.toString())
                        )
                        wind.add(w)
                        val h = Hourly(
                            cond_icon,
                            context.getString(R.string.time, i.toString()),
                            context.getString(R.string.degre, temp_hour.toString())
                        )
                        hourl.add(h)
                    }

                    val t = Today(cityName,last_updated,context.getString(R.string.degre,temp_c),condition_text,humidity,uv,context.getString(R.string.totalPrecip,totalPrecip),wind_kp,wind_degree,windDirection)


                    today.value = t
                    hourly.value = hourl
                    rainy.value = rainn
                    windy.value = wind

                    weatherError.value = false
                    weatherLoading.value = false
                }

                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    weatherLoading.value=false
                    weatherError.value=true
                    t.printStackTrace()
                }

            })
    }
}

