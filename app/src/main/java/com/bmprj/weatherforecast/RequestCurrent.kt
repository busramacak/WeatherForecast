package com.bmprj.weatherforecast

import android.R.attr.*
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
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
import com.bmprj.weatherforecast.databinding.FragmentTodayBinding
import com.google.android.gms.location.FusedLocationProviderClient
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class RequestCurrent(val view: View, val mFusedLocationClient:FusedLocationProviderClient) {


    var str="https://api.weatherapi.com/v1/forecast.json?key=3d94ea89afba4d1b8bf85744232605&q="
    val permissionId=2
    var time=""
    var tempature=""
    var conditionText=""

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission", "SetTextI18n")
    fun getLocation(binding:FragmentTodayBinding,dialog:AlertDialog, cityname:String?){


        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener() { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(view.context, Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)!!


                        if(cityname!=null){
                            if(cityname=="Konumunuz"){
                                str = str+"${list[0].latitude},${list[0].longitude}&days=1&aqi=yes&lang=tr"
                            }
                            else{
                                str = str+"${cityname}&days=1&aqi=yes&lang=tr"
                            }


                        }else{
                            str = str+"${list[0].latitude},${list[0].longitude}&days=1&aqi=yes&lang=tr"

                        }

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

                            val location = obj.getJSONObject("location")
                            val city = location.getString("name")
                            val dh =DatabaseHelper(view.context)
//                            var context: Context = view.context
//                            val intent = Intent(context,MainActivity::class.java)
                            DAO().update(dh,1,city)
//                            context.startActivity(intent)



                            val current = obj.getJSONObject("current")
                            time = current.getString("last_updated")
                            tempature = current.getDouble("temp_c").toInt().toString()

                            val conditionn=current.getJSONObject("condition")
                            val code = conditionn.getInt("code")
                            conditionText=conditionn.getString("text")

                            val forecast =obj.getJSONObject("forecast")
                            val forecastday  = forecast.getJSONArray("forecastday")
                            val hour = forecastday.getJSONObject(0)
                            val hh = hour.getJSONArray("hour")

                            val day = hour.getJSONObject("day")
                            val avghumidity=day.getInt("avghumidity")
                            val totalprecip_mm = day.getDouble("totalprecip_mm")
                            val uv = day.getInt("uv")


                            val formatter = DateTimeFormatter.ofPattern("HH")
                            val currentt = LocalDateTime.now().format(formatter)
                            val t = currentt.toInt()




                            var wind_kp :Double=0.0
                            var wind_degre:Int=0
                            var windDirection=""

                            val hourly = ArrayList<Hourly>()
                            val rainy = ArrayList<Rainy>()
                            val wind = ArrayList<Wind>()
                            if(t<17){
                                for(i  in t .. hh.length()-1){
                                    val hourSet = hh.getJSONObject(i)
                                    val hf = hh.getJSONObject(t)
                                    wind_kp = hf.getDouble("wind_kph")
                                    wind_degre = hf.getInt("wind_degree")
                                    windDirection = hf.getString("wind_dir")

                                    val temp = hourSet.getDouble("temp_c")
                                    val cond = hourSet.getJSONObject("condition")
                                    val icon = cond.getString("icon")
                                    val rain =hourSet.getInt("chance_of_rain")
                                    val precip = hourSet.getDouble("precip_mm").toFloat()
                                    val wind_degree=hourSet.getInt("wind_degree")
                                    val wind_kph = hourSet.getDouble("wind_kph")

                                    val r = Rainy("%"+rain.toString(),i.toString()+":00",precip.toString(),precip)
                                    rainy.add(r)
                                    val w = Wind(wind_kph.toString(),wind_kph.toInt()*3, wind_degree.toFloat(),i.toString()+":00")
                                    wind.add(w)
                                    val h = Hourly(icon,i.toString()+":00",temp.toString()+"°")
                                    hourly.add(h)
                                }
                            } else{
                                for(i  in 17 .. hh.length()-1){
                                    val hourSet = hh.getJSONObject(i)

                                    val hf = hh.getJSONObject(t)
                                    wind_kp = hf.getDouble("wind_kph")
                                    wind_degre = hf.getInt("wind_degree")
                                    windDirection = hf.getString("wind_dir")

                                    val temp = hourSet.getDouble("temp_c")
                                    val cond = hourSet.getJSONObject("condition")
                                    val icon = cond.getString("icon")
                                    val rain =hourSet.getInt("chance_of_rain")
                                    val precip = hourSet.getDouble("precip_mm").toFloat()
                                    val wind_degree=hourSet.getInt("wind_degree")
                                    val wind_kph = hourSet.getDouble("wind_kph")

                                    val r = Rainy("%"+rain.toString(),i.toString()+":00",precip.toString(),precip)
                                    rainy.add(r)
                                    val w = Wind(wind_kph.toString(),wind_kph.toInt()*3, wind_degree.toFloat(),i.toString()+":00")
                                    wind.add(w)
                                    val h = Hourly(icon,i.toString()+":00",temp.toString()+"°")
                                    hourly.add(h)
                                }

                            }

                            binding.recyWind.apply {
                                layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                                binding.recyWind.layoutManager=layoutManager
                                adapter= WindAdapter(wind)
                                binding.recyWind.adapter=adapter
                            }

                            binding.recyRain.apply {
                                layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                                binding.recyRain.layoutManager=layoutManager
                                adapter= RainyAdapter(rainy)
                                binding.recyRain.adapter=adapter
                            }


                            binding.recy.apply {
                                layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                                binding.recy.layoutManager=layoutManager
                                adapter= HourlyAdapter(hourly)
                                binding.recy.adapter=adapter
                            }



                            binding.date.text=time
                            binding.degree.text=tempature+"°"
                            binding.condition.text=conditionText
                            binding.humidity.text="%"+avghumidity.toString()
                            binding.uv.text=uv.toString()
                            binding.totalprecip.text="Günlük toplam hacim "+totalprecip_mm.toString()+" mm"
                            binding.windKph.text=wind_kp.toString()
                            binding.windDir.rotation=wind_degre.toFloat()
                            binding.direction.text=windDirection
                            when(code){
                                1000->{
                                    binding.scrollV.setBackgroundResource(R.color.cloudBackground)
                                    binding.scrollV.setBackgroundResource(R.color.sunBackground)
                                    binding.recy.setBackgroundResource(R.color.sunBackground)
                                    binding.recyWind.setBackgroundResource(R.color.sunBackground)
                                    binding.recyRain.setBackgroundResource(R.color.sunBackground)
                                    binding.relRain.setBackgroundResource(R.color.sunBackground)
                                    binding.rel.setBackgroundResource(R.color.sunBackground)
                                    binding.relWind.setBackgroundResource(R.color.sunBackground)

                                    if(t>6&&t<21){ binding.animationView.setAnimation(R.raw.sunny) }
                                    else { binding.animationView.setAnimation(R.raw.night) }
                                }
                                1003->{
                                    binding.scrollV.setBackgroundResource(R.color.cloudBackground)
                                    binding.recy.setBackgroundResource(R.color.cloudBackground)
                                    binding.recyWind.setBackgroundResource(R.color.cloudBackground)
                                    binding.recyRain.setBackgroundResource(R.color.cloudBackground)
                                    binding.relRain.setBackgroundResource(R.color.cloudBackground)
                                    binding.rel.setBackgroundResource(R.color.cloudBackground)
                                    binding.relWind.setBackgroundResource(R.color.cloudBackground)
                                    if(t>6&&t<21) { binding.animationView.setAnimation(R.raw.partly_cloudy) }
                                    else{binding.animationView.setAnimation(R.raw.cloudynight) }
                                }
                                1006->{
                                    binding.animationView.setAnimation(R.raw.cloudy)
                                    binding.scrollV.setBackgroundResource(R.color.cloudBackground)
                                    binding.recy.setBackgroundResource(R.color.cloudBackground)
                                    binding.recyWind.setBackgroundResource(R.color.cloudBackground)
                                    binding.recyRain.setBackgroundResource(R.color.cloudBackground)
                                    binding.relRain.setBackgroundResource(R.color.cloudBackground)
                                    binding.rel.setBackgroundResource(R.color.cloudBackground)
                                    binding.relWind.setBackgroundResource(R.color.cloudBackground)
                                }
                                1030,1135,1147->{ binding.animationView.setAnimation(R.raw.mist) }
                                1114, 1117, 1204, 1207, 1213, 1219, 1225 -> {
                                    if (t > 6 && t < 21) { binding.animationView.setAnimation(R.raw.snow) }
                                    else{ binding.animationView.setAnimation(R.raw.snownight)  }
                                }
                                1210,1216,1222,1249,1252,1255,1258 ->{ binding.animationView.setAnimation(
                                    R.raw.snow_sunny
                                ) }
                                1087,1273,1276->{ binding.animationView.setAnimation(R.raw.thunder) }
                                1183,1186,1189,1192,1195,1198,1201,1240,1246->{
                                    binding.animationView.setAnimation(R.raw.partly_shower)
                                    binding.scrollV.setBackgroundResource(R.color.rainyBackground)
                                    binding.recy.setBackgroundResource(R.color.rainyBackground)
                                    binding.recyWind.setBackgroundResource(R.color.rainyBackground)
                                    binding.recyRain.setBackgroundResource(R.color.rainyBackground)
                                    binding.relRain.setBackgroundResource(R.color.rainyBackground)
                                    binding.rel.setBackgroundResource(R.color.rainyBackground)
                                    binding.relWind.setBackgroundResource(R.color.rainyBackground)
                                }

                            }

                            dialog.hide()

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

}

