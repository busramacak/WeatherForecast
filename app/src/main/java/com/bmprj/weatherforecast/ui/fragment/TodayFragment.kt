@file:Suppress("DEPRECATION")

package com.bmprj.weatherforecast.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.ui.base.BaseFragment
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.HourlyAdapter
import com.bmprj.weatherforecast.adapter.RainyAdapter
import com.bmprj.weatherforecast.adapter.WindAdapter
import com.bmprj.weatherforecast.data.db.DAO
import com.bmprj.weatherforecast.data.db.DataBase
import com.bmprj.weatherforecast.databinding.FragmentTodayBinding
import com.bmprj.weatherforecast.viewmodel.TodayViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*



class TodayFragment() : BaseFragment<FragmentTodayBinding>(R.layout.fragment_today) {
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)
    private lateinit var viewModel : TodayViewModel
    private val hourlyAdapter = HourlyAdapter(arrayListOf())
    private val rainyAdapter = RainyAdapter(arrayListOf())
    private val windAdapter = WindAdapter(arrayListOf())



    @RequiresApi(Build.VERSION_CODES.O)
    override fun setUpViews(view:View) {
        super.setUpViews(view)


        binding.today=this
        viewModel = ViewModelProviders.of(this@TodayFragment).get(TodayViewModel::class.java)



        binding.recyRain.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyRain.adapter=rainyAdapter

        binding.recyWind.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyWind.adapter=windAdapter

        binding.recy.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recy.adapter=hourlyAdapter

        uiScope.launch(Dispatchers.Main){
            val dh = DataBase.getInstance(requireContext())
            val search = DAO().get(dh)
            val city:String?


            val dialog = ProgressDialog(context)
            dialog.setMessage(getString(R.string.yukleniyor))
            dialog.setCancelable(false)
            dialog.setInverseBackgroundForced(false)
//            dialog.show()

            if(search.size>0){
                for(i in search){
                    if(i.id==1){
                        if(i.search==null || i.search ==getString(R.string.mevcutKonum)){

                            getLocation(view,dialog)
                        }else{
                            city=i.search
                            getWeather(city)
                        }
                        break
                    }
                }
            }else{
                getLocation(view,dialog)
            }
        }

        observeLiveData()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeLiveData(){
        viewModel.hourly.observe(viewLifecycleOwner){ hourly ->
            hourly?.let{
                hourlyAdapter.updateList(hourly)
            }
        }

        viewModel.rainy.observe(viewLifecycleOwner) { rainy ->
            rainy?.let{
                rainyAdapter.updateList(rainy)
            }
        }
        viewModel.windy.observe(viewLifecycleOwner) { windy ->
            windy?.let{
                windAdapter.updateList(windy)
            }
        }

        val formatter = DateTimeFormatter.ofPattern("HH")
                val currentt = LocalDateTime.now().format(formatter)
                val t = currentt.toInt()

        viewModel.today.observe(viewLifecycleOwner) { today ->
            today?.let {
                binding.date.text = today.date
                binding.title.text = today.cityname
                binding.degree.text = today.degree
                binding.condition.text = today.conditionText
                binding.humidity.text = today.humidity
                binding.uv.text = today.uv
                binding.totalprecip.text = today.totalPrecip
                binding.windKph.text = today.wind_kph
                binding.windDir.rotation = today.wind_dir
                binding.direction.text = today.wind_direction

                when (today.code) {
                    1000 -> {
                        if (t > 6 && t < 21) {
                            binding.animationView.setAnimation(R.raw.sunny)
                        } else {
                            binding.animationView.setAnimation(R.raw.night)
                        }
                    }

                    1003 -> {
                        if (t > 6 && t < 21) {
                            binding.animationView.setAnimation(R.raw.partly_cloudy)
                        } else {
                            binding.animationView.setAnimation(R.raw.cloudynight)
                        }
                    }

                    1006 -> {
                        binding.animationView.setAnimation(R.raw.cloudy)
                    }

                    1030, 1135, 1147 -> {
                        binding.animationView.setAnimation(R.raw.mist)
                    }

                    1114, 1117, 1204, 1207, 1213, 1219, 1225 -> {
                        if (t > 6 && t < 21) {
                            binding.animationView.setAnimation(R.raw.snow)
                        } else {
                            binding.animationView.setAnimation(R.raw.snownight)
                        }
                    }

                    1210, 1216, 1222, 1249, 1252, 1255, 1258 -> {
                        binding.animationView.setAnimation(
                            R.raw.snow_sunny
                        )
                    }

                    1087, 1273, 1276 -> {
                        binding.animationView.setAnimation(R.raw.thunder)

                    }

                    1183, 1186, 1189, 1192, 1195, 1198, 1201, 1240, 1243, 1246 -> {
                        binding.animationView.setAnimation(R.raw.partly_shower)
                    }
                }
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeather(city:String?){

        viewModel.refreshData(requireContext(),"904aa43adf804caf913131326232306",city,1,"no",getString(R.string.lang))

//        val kdi = ApiUtils.getUrlInterface()
//        kdi.getWeather().enqueue(object : Callback<Weather>{
//            @RequiresApi(Build.VERSION_CODES.O)
//            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
//
//                Log.e("response",response.body().toString())
//                val current = response.body()?.current
//
//                val cityname = response.body()?.location?.name
//                val dh = DataBase.getInstance(dialog.context)
//
//                if(DAO().get(dh).size==0){
//                    DAO().add(dh,1,cityname)
//
//                }else
//                {
//                    DAO().update(dh,1,cityname)
//                }
//
//                val last_updated = current?.last_updated
//                val temp_c = current?.temp_c.toString()
//
//                val condition = current?.condition
//                val condition_text = condition?.text
//                val condition_code = condition?.code
//
//                val forecast = response.body()?.forecast
//                val hour = forecast?.forecastday?.get(0)?.hour
//
//                val day = forecast?.forecastday?.get(0)?.day
//                val avghumidity = day?.avghumidity.toString()
//                val totalprecip_mm = day?.totalprecip_mm
//                val uv = day?.uv
//
//                val formatter = DateTimeFormatter.ofPattern("HH")
//                val currentt = LocalDateTime.now().format(formatter)
//                val t = currentt.toInt()
//
//                var wind_kp =0.0
//                var wind_degree=0
//                var windDirection=""
//
//                val hourly = ArrayList<Hourly>()
//                val rainy = ArrayList<Rainy>()
//                val wind = ArrayList<Wind>()
//
//                if(t<17){
//
//                    for(i in t until hour!!.size){
//
//                        val hourSet = hour.get(i)
//                        val hf = hour.get(t)
//                        wind_kp = hf.wind_kph
//                        wind_degree = hf.wind_degree
//                        windDirection = hf.wind_dir
//
//                        val temp_hour = hourSet.temp_c
//                        val cond_hour = hourSet.condition
//                        val cond_icon = cond_hour.icon
//                        val rain = hourSet.chance_of_rain
//                        val precip_hour = hourSet.precip_mm
//                        val wind_degr_hour = hourSet.wind_degree
//                        val wind_kph_hour = hourSet.wind_kph
//
//                        val r = Rainy("%$rain",getString(R.string.time,i.toString()), precip_hour.toString(),precip_hour.toFloat())
//                        rainy.add(r)
//                        val w = Wind(wind_kph_hour.toString(),wind_kph_hour.toInt()*3,wind_degr_hour.toFloat(),getString(R.string.time,i.toString()))
//                        wind.add(w)
//                        val h = Hourly(cond_icon,getString(R.string.time,i.toString()),getString(R.string.degre,temp_hour.toString()))
//                        hourly.add(h)
//                    }
//                }
//                else{
//
//                    for( i in 17 until hour!!.size){
//
//                        val hourSet = hour.get(i)
//                        val hf = hour.get(t)
//                        wind_kp = hf.wind_kph
//                        wind_degree = hf.wind_degree
//                        windDirection = hf.wind_dir
//
//                        val temp_hour = hourSet.temp_c
//                        val cond_hour = hourSet.condition
//                        val cond_icon = cond_hour.icon
//                        val rain = hourSet.chance_of_rain
//                        val precip_hour = hourSet.precip_mm
//                        val wind_degr_hour = hourSet.wind_degree
//                        val wind_kph_hour = hourSet.wind_kph
//
//                        val r = Rainy("%$rain",getString(R.string.time,i.toString()), precip_hour.toString(),precip_hour.toFloat())
//                        rainy.add(r)
//                        val w = Wind(wind_kph_hour.toString(),wind_kph_hour.toInt()*3,wind_degr_hour.toFloat(),getString(R.string.time, i.toString()))
//                        wind.add(w)
//                        val h = Hourly(cond_icon,getString(R.string.time,i.toString()),getString(R.string.degre,temp_hour.toString()))
//                        hourly.add(h)
//                    }
//                }
//
//                dialog.dismiss()
//
//                binding.recyWind.apply {
//                    layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
//                    binding.recyWind.layoutManager=layoutManager
//                    adapter= WindAdapter(wind)
//                    binding.recyWind.adapter=adapter
//                }
//
//                binding.recyRain.apply {
//                    layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
//                    binding.recyRain.layoutManager=layoutManager
//                    adapter= RainyAdapter(rainy)
//                    binding.recyRain.adapter=adapter
//                }
//
//                binding.recy.apply {
//                    layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
//                    binding.recy.layoutManager=layoutManager
//                    adapter= HourlyAdapter(hourly)
//                    binding.recy.adapter=adapter
//                }
//
//                binding.title.text=cityname
//                binding.date.text = last_updated
//                binding.degree.text=getString(R.string.degre,temp_c)
//                binding.condition.text=condition_text
//                binding.humidity.text=getString(R.string.humidity,avghumidity)
//                binding.uv.text=uv.toString()
//                binding.totalprecip.text=getString(R.string.totalPrecip,totalprecip_mm.toString())
//                binding.windKph.text=wind_kp.toString()
//                binding.windDir.rotation=wind_degree.toFloat()
//                binding.direction.text=windDirection
//
//                when(condition_code){
//                    1000->{
//                        if(t>6&&t<21){ binding.animationView.setAnimation(R.raw.sunny) }
//                        else { binding.animationView.setAnimation(R.raw.night) }
//                    }
//                    1003->{
//                        if(t>6&&t<21) { binding.animationView.setAnimation(R.raw.partly_cloudy) }
//                        else{binding.animationView.setAnimation(R.raw.cloudynight) }
//                    }
//                    1006->{
//                        binding.animationView.setAnimation(R.raw.cloudy)
//                    }
//                    1030,1135,1147->{ binding.animationView.setAnimation(R.raw.mist) }
//                    1114, 1117, 1204, 1207, 1213, 1219, 1225 -> {
//                        if (t > 6 && t < 21) { binding.animationView.setAnimation(R.raw.snow) }
//                        else{ binding.animationView.setAnimation(R.raw.snownight)  }
//                    }
//                    1210,1216,1222,1249,1252,1255,1258 ->{ binding.animationView.setAnimation(
//                        R.raw.snow_sunny
//                    ) }
//                    1087,1273,1276->{
//                        binding.animationView.setAnimation(R.raw.thunder)
//
//                    }
//                    1183,1186,1189,1192,1195,1198,1201,1240,1243,1246->{
//                        binding.animationView.setAnimation(R.raw.partly_shower)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<Weather>, t: Throwable) {
//
//                Log.e("response",t.message.toString())
//            }
//        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    fun getLocation(view:View,dialog: AlertDialog){

        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        if (checkPermissions(view)) {
            if (isLocationEnabled(view)) {
                mFusedLocationClient.lastLocation.addOnCompleteListener() { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        getWeather("${location.latitude},${location.longitude}")
                    }
                }
            }else{
                Navigation.findNavController(view).navigate(R.id.searchFragment)
                dialog.dismiss()
            }
        } else {
            requestPermissions(view)
            Navigation.findNavController(view).navigate(R.id.searchFragment)
            dialog.dismiss()
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


//    @RequiresApi(Build.VERSION_CODES.O)
//    fun refreshCurrentClick(view:View){
//        onViewCreated(view, bundleOf())
//        val dialog = ProgressDialog(context)
//        dialog.setMessage(getString(R.string.yukleniyor))
//        dialog.setCancelable(false)
//        dialog.setInverseBackgroundForced(false)
//        dialog.show()
//        viewModel = ViewModelProviders.of(this@TodayFragment).get(TodayViewModel::class.java)
//
//
//        uiScope.launch(Dispatchers.Main){
//            val dh = DataBase.getInstance(requireContext())
//            val search = DAO().get(dh)
//            val city:String?
//            if(search.size>0){
//                for(i in search){
//                    if(i.id==1){
//                        city=i.search
//                        getWeather(city)
//                        break
//                    }
//                }
//            }else{
//                getLocation(view,dialog)
//            }
//        }
//
//        observeLiveData()
//    }


