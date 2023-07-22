package com.bmprj.weatherforecast.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.model.Hourly
import com.bmprj.weatherforecast.data.model.Rainy
import com.bmprj.weatherforecast.data.model.Tomorrow
import com.bmprj.weatherforecast.data.model.Weather
import com.bmprj.weatherforecast.data.model.Wind
import com.bmprj.weatherforecast.data.remote.ApiUtils
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class TomorrowViewModel:ViewModel() {
    private val weatherApiUtils = ApiUtils()


    val hourlyTom = MutableLiveData<ArrayList<Hourly>>()
    val rainyTom = MutableLiveData<ArrayList<Rainy>>()
    val windyTom = MutableLiveData<ArrayList<Wind>>()

    val tomorrow = MutableLiveData<Tomorrow>()

    fun refreshData(context: Context, key:String, q:String?, days:Int, aqi:String, lang:String){
        getDataFromApi(context,key, q, days, aqi, lang)

    }

    private fun getDataFromApi(context: Context, key:String, q:String?, days:Int, aqi:String, lang:String){
        val hourlyTomorrow= ArrayList<Hourly>()
        val rainyTomorrow = ArrayList<Rainy>()
        val windTomorrow= ArrayList<Wind>()


        weatherApiUtils.getData(key,q,days,aqi,lang).enqueue(object : Callback<Weather>{
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {


                val cityName = response.body()?.location?.name
                val forecastday = response.body()?.forecast?.forecastday?.get(1)
                val date = forecastday?.date
                val max_temp_c_tomorrow = forecastday?.day?.maxtemp_c.toString()
                val min_temp_c_tomorrow = forecastday?.day?.mintemp_c.toString()
                val conditionTextT = forecastday?.day?.condition?.text
                val conditioncodeT = forecastday?.day?.condition?.code
                val humidityT = forecastday?.day?.avghumidity.toString()
                val uvT = forecastday?.day?.uv.toString()
                val totalPrecipT = forecastday?.day?.totalprecip_mm.toString()


                val hourTomorrow = response.body()?.forecast?.forecastday?.get(1)?.hour

                var maxWind=0.0
                var minWind=0.0


                for(i in 0 until hourTomorrow!!.size){

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
                        context.getString(R.string.time,i.toString()),
                        precip_hour.toString(),
                        precip_hour.toFloat())

                    rainyTomorrow.add(r)

                    val w = Wind(
                        wind_kph_hour.toString(),
                        wind_kph_hour.toInt()*3,
                        wind_degr_hour.toFloat(),
                        context.getString(R.string.time,i.toString()))

                    windTomorrow.add(w)

                    val h = Hourly(
                        cond_icon,
                        context.getString(R.string.time,i.toString()),
                        context.getString(R.string.degre, temp_hour.toString()))

                    hourlyTomorrow.add(h)

                }

                val tom = Tomorrow(
                    cityName,
                    date,
                    context.getString(R.string.max_minTemp,max_temp_c_tomorrow,min_temp_c_tomorrow),
                    conditionTextT,
                    humidityT,
                    uvT,
                    context.getString(R.string.totalPrecip,totalPrecipT),
                    context.getString(R.string.max_minWind,minWind.toInt().toString(),maxWind.toInt().toString()),
                    conditioncodeT)

                tomorrow.value=tom

                hourlyTom.value = hourlyTomorrow
                rainyTom.value=rainyTomorrow
                windyTom.value=windTomorrow



            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

}


