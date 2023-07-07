@file:Suppress("DEPRECATION")

package com.bmprj.weatherforecast.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.HourlyAdapter
import com.bmprj.weatherforecast.adapter.RainyAdapter
import com.bmprj.weatherforecast.adapter.WindAdapter
import com.bmprj.weatherforecast.data.db.DAO
import com.bmprj.weatherforecast.data.db.DatabaseHelper
import com.bmprj.weatherforecast.data.remote.ApiUtils
import com.bmprj.weatherforecast.model.Hourly
import com.bmprj.weatherforecast.model.Rainy
import com.bmprj.weatherforecast.model.Weather
import com.bmprj.weatherforecast.model.Wind
import com.bmprj.weatherforecast.databinding.FragmentTodayBinding
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class TodayFragment() : Fragment() {
    private lateinit var binding: FragmentTodayBinding
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_today, container, false)
        binding.today=this



        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshCurrentClick(view:View){
        onViewCreated(view, bundleOf())
        val dialog = ProgressDialog(context)
        dialog.setMessage("Yükleniyor...")
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.show()

        uiScope.launch(Dispatchers.Main){
            val dh = DatabaseHelper(requireContext())
            val search = DAO().get(dh)
            val city:String?
            if(search.size>0){
                for(i in search){
                    if(i.id==1){
                        city=i.search
                        getWeather(city,dialog)
                        break
                    }
                }
            }else{
                getLocation(view,dialog)
            }

        }



    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        uiScope.launch(Dispatchers.Main){
            val dh = DatabaseHelper(requireContext())
            val search = DAO().get(dh)
            val city:String?

            val dialog = ProgressDialog(context)
            dialog.setMessage("Yükleniyor...")
            dialog.setCancelable(false)
            dialog.setInverseBackgroundForced(false)
            dialog.show()

            if(search.size>0){
                for(i in search){
                    if(i.id==1){
                        if(i.search==null || i.search =="Mevcut Konum"){

                            getLocation(view,dialog)
                        }else{
                            city=i.search
                            getWeather(city,dialog)
                        }
                        break
                    }
                }
            }else{
                getLocation(view,dialog)
            }
        }


    }


    fun getWeather(city:String?,dialog: AlertDialog){

        val kdi = ApiUtils.getUrlInterface()
        kdi.getWeather("904aa43adf804caf913131326232306",city,1,"no","tr").enqueue(object : Callback<Weather>{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

                Log.e("response",response.body().toString())
                val current = response.body()?.current

                val cityname = response.body()?.location?.name
                val dh = DatabaseHelper(dialog.context)

                if(DAO().get(dh).size==0){
                    DAO().add(dh,1,cityname)

                }else
                {
                    DAO().update(dh,1,cityname)
                }

                val last_updated = current?.last_updated
                val temp_c = current?.temp_c.toString()

                val condition = current?.condition
                val condition_text = condition?.text
                val condition_code = condition?.code

                val forecast = response.body()?.forecast
                val hour = forecast?.forecastday?.get(0)?.hour

                val day = forecast?.forecastday?.get(0)?.day
                val avghumidity = day?.avghumidity.toString()
                val totalprecip_mm = day?.totalprecip_mm
                val uv = day?.uv

                val formatter = DateTimeFormatter.ofPattern("HH")
                val currentt = LocalDateTime.now().format(formatter)
                val t = currentt.toInt()

                var wind_kp =0.0
                var wind_degree=0
                var windDirection=""

                val hourly = ArrayList<Hourly>()
                val rainy = ArrayList<Rainy>()
                val wind = ArrayList<Wind>()

                if(t<17){


                    for(i in t until hour!!.size){

                        val hourSet = hour.get(i)
                        val hf = hour.get(t)
                        wind_kp = hf.wind_kph
                        wind_degree = hf.wind_degree
                        windDirection = hf.wind_dir

                        val temp_hour = hourSet.temp_c
                        val cond_hour = hourSet.condition
                        val cond_icon = cond_hour.icon
                        val rain = hourSet.chance_of_rain
                        val precip_hour = hourSet.precip_mm
                        val wind_degr_hour = hourSet.wind_degree
                        val wind_kph_hour = hourSet.wind_kph

                        val r = Rainy("%$rain",getString(R.string.time,i.toString()), precip_hour.toString(),precip_hour.toFloat())
                        rainy.add(r)
                        val w = Wind(wind_kph_hour.toString(),wind_kph_hour.toInt()*3,wind_degr_hour.toFloat(),getString(R.string.time,i.toString()))
                        wind.add(w)
                        val h = Hourly(cond_icon,getString(R.string.time,i.toString()),getString(R.string.degre,temp_hour.toString()))
                        hourly.add(h)

                    }
                }
                else{

                    for( i in 17 until hour!!.size){

                        val hourSet = hour.get(i)
                        val hf = hour.get(t)
                        wind_kp = hf.wind_kph
                        wind_degree = hf.wind_degree
                        windDirection = hf.wind_dir

                        val temp_hour = hourSet.temp_c
                        val cond_hour = hourSet.condition
                        val cond_icon = cond_hour.icon
                        val rain = hourSet.chance_of_rain
                        val precip_hour = hourSet.precip_mm
                        val wind_degr_hour = hourSet.wind_degree
                        val wind_kph_hour = hourSet.wind_kph

                        val r = Rainy("%$rain",getString(R.string.time,i.toString()), precip_hour.toString(),precip_hour.toFloat())
                        rainy.add(r)
                        val w = Wind(wind_kph_hour.toString(),wind_kph_hour.toInt()*3,wind_degr_hour.toFloat(),getString(R.string.time, i.toString()))
                        wind.add(w)
                        val h = Hourly(cond_icon,getString(R.string.time,i.toString()),getString(R.string.degre,temp_hour.toString()))
                        hourly.add(h)

                    }
                }


                dialog.dismiss()


                binding.recyWind.apply {
                    layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
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

                binding.title.text=cityname
                binding.date.text = last_updated
                binding.degree.text=getString(R.string.degre,temp_c)
                binding.condition.text=condition_text
                binding.humidity.text=getString(R.string.humidity,avghumidity)
                binding.uv.text=uv.toString()
                binding.totalprecip.text=getString(R.string.totalPrecip,totalprecip_mm.toString())
                binding.windKph.text=wind_kp.toString()
                binding.windDir.rotation=wind_degree.toFloat()
                binding.direction.text=windDirection

                when(condition_code){
                    1000->{
                        if(t>6&&t<21){ binding.animationView.setAnimation(R.raw.sunny) }
                        else { binding.animationView.setAnimation(R.raw.night) }
                    }
                    1003->{
                        if(t>6&&t<21) { binding.animationView.setAnimation(R.raw.partly_cloudy) }
                        else{binding.animationView.setAnimation(R.raw.cloudynight) }
                    }
                    1006->{
                        binding.animationView.setAnimation(R.raw.cloudy)
                    }
                    1030,1135,1147->{ binding.animationView.setAnimation(R.raw.mist) }
                    1114, 1117, 1204, 1207, 1213, 1219, 1225 -> {
                        if (t > 6 && t < 21) { binding.animationView.setAnimation(R.raw.snow) }
                        else{ binding.animationView.setAnimation(R.raw.snownight)  }
                    }
                    1210,1216,1222,1249,1252,1255,1258 ->{ binding.animationView.setAnimation(
                        R.raw.snow_sunny
                    ) }
                    1087,1273,1276->{
                        binding.animationView.setAnimation(R.raw.thunder)

                    }
                    1183,1186,1189,1192,1195,1198,1201,1240,1243,1246->{
                        binding.animationView.setAnimation(R.raw.partly_shower)
                    }
                }


            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {

                Log.e("response",t.message.toString())
            }
        })

    }

    @SuppressLint("MissingPermission")
    fun getLocation(view:View,dialog: AlertDialog){

        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        if (checkPermissions(view)) {
            if (isLocationEnabled(view)) {
                mFusedLocationClient.lastLocation.addOnCompleteListener() { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        dialog.dismiss()
                        getWeather("${location.latitude},${location.longitude}", dialog)

                    }
                }
            }else{
                dialog.dismiss()
            }
        } else {
            requestPermissions(view)

        }
    }

    fun isLocationEnabled(view:View): Boolean {
        val locationManager: LocationManager =
            view.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun checkPermissions(view:View): Boolean {
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
    fun requestPermissions(view:View) {
        ActivityCompat.requestPermissions(
            view.context as Activity,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            2
        )
    }


}


