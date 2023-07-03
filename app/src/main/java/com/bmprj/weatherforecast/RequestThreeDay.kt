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
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bmprj.weatherforecast.Model.ThreeDay
import com.bmprj.weatherforecast.databinding.FragmentThreeDayBinding
import com.google.android.gms.location.FusedLocationProviderClient
import org.json.JSONObject
import java.net.URLDecoder
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class RequestThreeDay(val view: View, val mFusedLocationClient:FusedLocationProviderClient) {


    var str="https://api.weatherapi.com/v1/forecast.json?key=904aa43adf804caf913131326232306&q="
    val permissionId=2

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission", "SetTextI18n", "SimpleDateFormat")
    fun getLocation(binding: FragmentThreeDayBinding,dialog: AlertDialog,cityname:String?){

        val queue = Volley.newRequestQueue(view.context)

        if(cityname!="Mevcut Konum" && cityname!=null){

            str = str+"${cityname}&days=3&aqi=yes&lang=tr"

            val req = StringRequest(com.android.volley.Request.Method.GET,str,{
                response ->

                val json = URLDecoder.decode(URLEncoder.encode(response, "iso8859-1"),"UTF-8")

                val obj = JSONObject(json)

                val forecast =obj.getJSONObject("forecast")
                val forecastday  = forecast.getJSONArray("forecastday")


                val threeday = ArrayList<ThreeDay>()
                for(i in 0..2){

                    val hour = forecastday.getJSONObject(i)

                    val day = hour.getJSONObject("day")
                    val date = hour.getString("date")

                    val inFormat = SimpleDateFormat("yyyy-MM-dd")
                    val dat: Date = inFormat.parse(date) as Date
                    val outFormatDays = SimpleDateFormat("EEEE")
                    val goal: String = outFormatDays.format(dat)
                    val outFormatMonth = SimpleDateFormat("MMM")
                    val month:String = outFormatMonth.format(dat)
                    val outFormatDay = SimpleDateFormat("dd")
                    val dy : String = outFormatDay.format(dat)



                    val maxtemp=day.getDouble("maxtemp_c")
                    val mintemp=day.getDouble("mintemp_c")

                    val condition = day.getJSONObject("condition")
                    val icon = condition.getString("icon")
                    val conditionText = condition.getString("text")

                    val t = ThreeDay(goal+", "+month+" "+dy,conditionText,maxtemp.toString()+"°",mintemp.toString()+"°",icon)

                    threeday.add(t)

                }

                binding.recyThreeDay.apply {
                    layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                    binding.recyThreeDay.layoutManager=layoutManager
                    adapter = ThreeDayAdapter(threeday)
                    binding.recyThreeDay.adapter=adapter
                }

            },{ })

            queue.add(req)

            dialog.hide()


        }else{

            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    mFusedLocationClient.lastLocation.addOnCompleteListener() { task ->
                        val location: Location? = task.result
                        if (location != null) {
                            val geocoder = Geocoder(view.context, Locale.getDefault())
                            val list: List<Address> =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)!!

                            if(cityname!=null){
                                if(cityname=="Mevcut Konum"){
                                    str += "${list[0].latitude},${list[0].longitude}&days=3&aqi=yes&lang=tr"
                                }
                                else{
                                    str += "${cityname}&days=3&aqi=yes&lang=tr"
                                }


                            }else{
                                str += "${list[0].latitude},${list[0].longitude}&days=3&aqi=yes&lang=tr"

                            }



                            val req = StringRequest(com.android.volley.Request.Method.GET,str,{
                                response->

                                val json = URLDecoder.decode(URLEncoder.encode(response, "iso8859-1"),"UTF-8")

                                val obj = JSONObject(json)

                                val forecast =obj.getJSONObject("forecast")
                                val forecastday  = forecast.getJSONArray("forecastday")


                                val threeday = ArrayList<ThreeDay>()
                                for(i in 0..2){

                                    val hour = forecastday.getJSONObject(i)

                                    val day = hour.getJSONObject("day")
                                    val date = hour.getString("date")

                                    val inFormat = SimpleDateFormat("yyyy-MM-dd")
                                    val dat: Date = inFormat.parse(date) as Date
                                    val outFormatDays = SimpleDateFormat("EEEE")
                                    val goal: String = outFormatDays.format(dat)
                                    val outFormatMonth = SimpleDateFormat("MMM")
                                    val month:String = outFormatMonth.format(dat)
                                    val outFormatDay = SimpleDateFormat("dd")
                                    val dy : String = outFormatDay.format(dat)



                                    val maxtemp=day.getDouble("maxtemp_c")
                                    val mintemp=day.getDouble("mintemp_c")

                                    val condition = day.getJSONObject("condition")
                                    val icon = condition.getString("icon")
                                    val conditionText = condition.getString("text")

                                    val t = ThreeDay(goal+", "+month+" "+dy,conditionText,maxtemp.toString()+"°",mintemp.toString()+"°",icon)

                                    threeday.add(t)

                                }

                                binding.recyThreeDay.apply {
                                    layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                                    binding.recyThreeDay.layoutManager=layoutManager
                                    adapter = ThreeDayAdapter(threeday)
                                    binding.recyThreeDay.adapter=adapter
                                }


                            }, {   })

                            queue.add(req)
                            dialog.hide()

                        }
                    }
                }
            } else {
                requestPermissions()
            }
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

