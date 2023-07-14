@file:Suppress("DEPRECATION")

package com.bmprj.weatherforecast.ui.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.ui.base.BaseFragment
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.HourlyAdapter
import com.bmprj.weatherforecast.adapter.RainyAdapter
import com.bmprj.weatherforecast.adapter.WindAdapter
import com.bmprj.weatherforecast.data.remote.ApiUtils
import com.bmprj.weatherforecast.data.db.DAO
import com.bmprj.weatherforecast.data.db.DataBase
import com.bmprj.weatherforecast.data.model.Hourly
import com.bmprj.weatherforecast.data.model.Rainy
import com.bmprj.weatherforecast.data.model.Weather
import com.bmprj.weatherforecast.data.model.Wind
import com.bmprj.weatherforecast.databinding.FragmentTomorrowBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

@Suppress("DEPRECATION")
class TomorrowFragment : BaseFragment<FragmentTomorrowBinding>(R.layout.fragment_tomorrow) {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main+job)

    override fun setUpViews(view: View) {
        super.setUpViews(view)

        binding.tomorrow=this

        val dialog = ProgressDialog(context)
        dialog.setMessage(getString(R.string.yukleniyor))
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.show()

        uiScope.launch(Dispatchers.Main){
            val dh = DataBase.getInstance(requireContext())
            val search = DAO().get(dh)
            val city:String?
            for(i in search){
                if(i.id==1){
                    city=i.search
                    getWeather(city,dialog)

                    break
                }
            }
        }

    }

    private fun getWeather(city:String?, dialog: AlertDialog){

        val kdi = ApiUtils.getUrlInterface()
        kdi.getWeather("904aa43adf804caf913131326232306",city,2,"no",getString(R.string.lang)).enqueue(object :
            Callback<Weather> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

                Log.e("response",response.body().toString())
                val current = response.body()?.current


                val condition = current?.condition
                val condition_text = condition?.text
                val condition_code = condition?.code

                val forecast = response.body()?.forecast
                val hour = forecast?.forecastday?.get(1)?.hour
                val date = forecast?.forecastday?.get(1)?.date
                val day = forecast?.forecastday?.get(0)?.day
                val max_temp = day?.maxtemp_c.toString()
                val min_temp = day?.mintemp_c.toString()
                val avghumidity = day?.avghumidity.toString()
                val totalprecip_mm = day?.totalprecip_mm.toString()
                val uv = day?.uv


                val hourly = ArrayList<Hourly>()
                val rainy = ArrayList<Rainy>()
                val wind = ArrayList<Wind>()
                var maxWind=0.0
                var minWind=0.0

                for(i in 0 until hour!!.size){

                    val hourSet = hour[i]

                    val temp_hour = hourSet.temp_c
                    val cond_hour = hourSet.condition
                    val cond_icon = cond_hour.icon
                    val rain = hourSet.chance_of_rain
                    val precip_hour = hourSet.precip_mm
                    val wind_degr_hour = hourSet.wind_degree
                    val wind_kph_hour = hourSet.wind_kph

                    if(minWind>wind_kph_hour) minWind=wind_kph_hour

                    if(maxWind< wind_kph_hour) maxWind=wind_kph_hour

                    val r = Rainy("%$rain",getString(R.string.time,i.toString()), precip_hour.toString(),precip_hour.toFloat())
                    rainy.add(r)
                    val w = Wind(wind_kph_hour.toString(),wind_kph_hour.toInt()*3,wind_degr_hour.toFloat(),getString(R.string.time,i.toString()))
                    wind.add(w)
                    val h = Hourly(cond_icon,getString(R.string.time,i.toString()),getString(R.string.degre,temp_hour.toString()))
                    hourly.add(h)

                }



                binding.recyWind.apply {
                    layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                    binding.recyWind.layoutManager=layoutManager
                    adapter= WindAdapter(wind)
                    binding.recyWind.adapter=adapter
                }

                binding.recyRain.apply {
                    layoutManager = LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL,false)
                    binding.recyRain.layoutManager=layoutManager
                    adapter= RainyAdapter(rainy)
                    binding.recyRain.adapter=adapter
                }


                binding.recy.apply {
                    layoutManager = LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL,false)
                    binding.recy.layoutManager=layoutManager
                    adapter= HourlyAdapter(hourly)
                    binding.recy.adapter=adapter
                }

                binding.title.text=city
                binding.date.text = date
                binding.degree.text=getString(R.string.max_minTemp,max_temp,min_temp)
                binding.condition.text=condition_text
                binding.humidity.text=getString(R.string.humidity,avghumidity)
                binding.uv.text=uv.toString()
                binding.totalprecip.text=getString(R.string.totalPrecip,totalprecip_mm)
                binding.windKph.text=getString(R.string.max_minWind,minWind.toInt().toString(),maxWind.toInt().toString())

                when(condition_code){
                    1000->{ binding.animationView.setAnimation(R.raw.sunny) }
                    1003->{ binding.animationView.setAnimation(R.raw.partly_cloudy) }
                    1006->{ binding.animationView.setAnimation(R.raw.cloudy) }
                    1030,1135,1147->{ binding.animationView.setAnimation(R.raw.mist) }
                    1114, 1117, 1204, 1207, 1213, 1219, 1225 -> {
                        binding.animationView.setAnimation(R.raw.snow)
                    }
                    1210,1216,1222,1249,1252,1255,1258 ->{
                        binding.animationView.setAnimation(R.raw.snow_sunny)
                    }
                    1087,1273,1276->{
                        binding.animationView.setAnimation(R.raw.thunder)

                    }
                    1183,1186,1189,1192,1195,1198,1201,1240,1243,1246->{
                        binding.animationView.setAnimation(R.raw.partly_shower)
                    }
                }

                dialog.dismiss()
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {

                Log.e("tomorrow",t.message.toString())
            }
        })

    }

    override fun onResume() {
        super.onResume()
        binding.animationView.playAnimation()
    }
}