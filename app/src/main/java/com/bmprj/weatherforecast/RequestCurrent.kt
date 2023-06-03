package com.bmprj.weatherforecast

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.StrictMode
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.location.FusedLocationProviderClient
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class RequestCurrent(val view: View, val mFusedLocationClient:FusedLocationProviderClient) {


    var str="https://api.weatherapi.com/v1/forecast.json?key=3d94ea89afba4d1b8bf85744232605&q="
    val permissionId=2
    var time=""
    var tempature=""
    var conditionText=""

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission", "SetTextI18n")
    fun getLocation(date:TextView,degree:TextView,condition:TextView,animationView:LottieAnimationView,recy:RecyclerView){

        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener() { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(view.context, Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)!!

                        str = str+"${list[0].latitude},${list[0].longitude}&days=1&aqi=yes&lang=tr"

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
                            
// pressure_mb, humidity,uv,chance_of_rain eklenecek!!

                            val formatter = DateTimeFormatter.ofPattern("HH")
                            val currentt = LocalDateTime.now().format(formatter)
                            val t = currentt.toInt()

                            val liste=ArrayList<Hourly>()
                            if(t<17){
                                for(i  in t .. hh.length()-1){
                                    val hourSet = hh.getJSONObject(i)
                                    val temp = hourSet.getDouble("temp_c")
                                    val cond = hourSet.getJSONObject("condition")
                                    val icon = cond.getString("icon")

                                    val h = Hourly(icon,i.toString()+":00",temp.toString()+"°")
                                    liste.add(h)
                                }
                            } else{
                                for(i  in 17 .. hh.length()-1){
                                    val hourSet = hh.getJSONObject(i)
                                    val temp = hourSet.getDouble("temp_c")
                                    val cond = hourSet.getJSONObject("condition")
                                    val icon = cond.getString("icon")

                                    val h = Hourly(icon,i.toString()+":00",temp.toString()+"°")
                                    liste.add(h)
                                }

                            }


                                recy.apply {
                                    layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                                    recy.layoutManager=layoutManager
                                    adapter=HourlyAdapter(liste)
                                    recy.adapter=adapter
                                }


                            date.text=time
                            degree.text=tempature+"°"
                            condition.text=conditionText

                            when(code){
                                1000->{ animationView.setAnimation(R.raw.sunny) }
                                1003->{ animationView.setAnimation(R.raw.partly_cloudy) }
                                1006->{ animationView.setAnimation(R.raw.cloudy) }
                                1030,1135,1147->{ animationView.setAnimation(R.raw.mist) }
                                1114,1117,1204,1207,1213,1219,1225,->{ animationView.setAnimation(R.raw.snow) }
                                1210,1216,1222,1249,1252,1255,1258 ->{ animationView.setAnimation(R.raw.snow_sunny) }
                                1087,1273,1276->{ animationView.setAnimation(R.raw.thunder) }
                                1183,1186,1189,1192,1195,1198,1201,1240,1246->{ animationView.setAnimation(R.raw.partly_shower) }

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

