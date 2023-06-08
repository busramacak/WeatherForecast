package com.bmprj.weatherforecast

import android.R.attr.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.StrictMode
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.databinding.FragmentTomorrowBinding
import com.google.android.gms.location.FusedLocationProviderClient
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.*


class RequestTomorrow (val view: View, val mFusedLocationClient:FusedLocationProviderClient) {


    var str="https://api.weatherapi.com/v1/forecast.json?key=3d94ea89afba4d1b8bf85744232605&q="
    val permissionId=2

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission", "SetTextI18n")
    fun getLocation(binding: FragmentTomorrowBinding){


        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener() { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(view.context, Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)!!

                        str = str+"${list[0].latitude},${list[0].longitude}&days=2&aqi=yes&lang=tr"

                        val SDK_INT = Build.VERSION.SDK_INT
                        if (SDK_INT > 8) {
                            val policy = StrictMode.ThreadPolicy.Builder()
                                .permitAll().build()
                            StrictMode.setThreadPolicy(policy)


                            var client= OkHttpClient()
                            val request = Request.Builder().url(str).build()
                            val response = client.newCall(request).execute()

                            val json = response.body!!.string()
                            val obj = JSONObject(json)
//                            val current = obj.getJSONObject("current")
//                            time = current.getString("last_updated")
//                            tempature = current.getDouble("temp_c").toInt().toString()
//
//                            val conditionn=current.getJSONObject("condition")
//                            val code = conditionn.getInt("code")
//                            conditionText=conditionn.getString("text")

                            val forecast =obj.getJSONObject("forecast")
                            val forecastday  = forecast.getJSONArray("forecastday")
                            val hour = forecastday.getJSONObject(1)
                            val hh = hour.getJSONArray("hour")

                            val date = hour.getString("date")

                            val day = hour.getJSONObject("day")
                            val avghumidity=day.getInt("avghumidity")
                            val totalprecip_mm = day.getDouble("totalprecip_mm")
                            val uv = day.getInt("uv")
                            val max_temp = day.getDouble("maxtemp_c")
                            val min_temp = day.getDouble("mintemp_c")

                            val conditionn=day.getJSONObject("condition")
                            val code = conditionn.getInt("code")
                            val conditionText=conditionn.getString("text")


                            var wind_degre:Int=0


                            val hourly = ArrayList<Hourly>()
                            val rainy = ArrayList<Rainy>()
                            val wind = ArrayList<Wind>()
                            var maxwind=0.0
                            var minwind=0.0

                                for(i  in 0 .. hh.length()-1){
                                    val hourSet = hh.getJSONObject(i)

                                    val temp = hourSet.getDouble("temp_c")
                                    val cond = hourSet.getJSONObject("condition")
                                    val icon = cond.getString("icon")
                                    val rain =hourSet.getInt("chance_of_rain")
                                    val precip = hourSet.getDouble("precip_mm").toFloat()
                                    val wind_degree=hourSet.getInt("wind_degree")
                                    val wind_kph = hourSet.getDouble("wind_kph")

                                    if(minwind>wind_kph){
                                        minwind=wind_kph
                                    }

                                    if(maxwind< wind_kph){
                                        maxwind=wind_kph
                                    }


                                    val r = Rainy("%"+rain.toString(),i.toString()+":00",precip.toString(),precip)
                                    rainy.add(r)
                                    val w = Wind(wind_kph.toString(),wind_kph.toInt()*3, wind_degree.toFloat(),i.toString()+":00")
                                    wind.add(w)
                                    val h = Hourly(icon,i.toString()+":00",temp.toString()+"°")
                                    hourly.add(h)
                                }



                            binding.recyWind.apply {
                                layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                                binding.recyWind.layoutManager=layoutManager
                                adapter=WindAdapter(wind)
                                binding.recyWind.adapter=adapter
                            }

                            binding.recyRain.apply {
                                layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                                binding.recyRain.layoutManager=layoutManager
                                adapter=RainyAdapter(rainy)
                                binding.recyRain.adapter=adapter
                            }


                            binding.recy.apply {
                                layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                                binding.recy.layoutManager=layoutManager
                                adapter=HourlyAdapter(hourly)
                                binding.recy.adapter=adapter
                            }



                            binding.date.text=date
                            binding.degree.text="Gündüz ${max_temp}, Gece ${min_temp}"
                            binding.condition.text=conditionText
                            binding.humidity.text="%"+avghumidity.toString()
                            binding.uv.text=uv.toString()
                            binding.totalprecip.text="Günlük toplam hacim "+totalprecip_mm.toString()+" mm"
                            binding.windKph.text=minwind.toInt().toString()+"-"+maxwind.toInt().toString()
                            when(code){
                                1000->{ binding.animationView.setAnimation(R.raw.sunny) }
                                1003->{ binding.animationView.setAnimation(R.raw.partly_cloudy) }
                                1006->{ binding.animationView.setAnimation(R.raw.cloudy) }
                                1030,1135,1147->{ binding.animationView.setAnimation(R.raw.mist) }
                                1114, 1117, 1204, 1207, 1213, 1219, 1225 -> { binding.animationView.setAnimation(R.raw.snow) }
                                1210,1216,1222,1249,1252,1255,1258 ->{ binding.animationView.setAnimation(R.raw.snow_sunny) }
                                1087,1273,1276->{ binding.animationView.setAnimation(R.raw.thunder) }
                                1183,1186,1189,1192,1195,1198,1201,1240,1246->{ binding.animationView.setAnimation(R.raw.partly_shower) }

                            }



                        }

                    }
                }
            }
        } else {
            requestPermissions()
        }

    }

    fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            view.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                view.context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                view.context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    fun requestPermissions() {
        ActivityCompat.requestPermissions(
            view.context as Activity,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    fun getData(list:ArrayList<String>):ArrayList<String>{

        var liste = ArrayList<String>()



        return liste

    }
}

