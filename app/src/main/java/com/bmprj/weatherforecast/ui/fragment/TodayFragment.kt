package com.bmprj.weatherforecast.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.HourlyAdapter
import com.bmprj.weatherforecast.adapter.RainyAdapter
import com.bmprj.weatherforecast.adapter.WindAdapter
import com.bmprj.weatherforecast.data.db.sqlite.DAO
import com.bmprj.weatherforecast.data.db.sqlite.DataBase
import com.bmprj.weatherforecast.databinding.FragmentTodayBinding
import com.bmprj.weatherforecast.base.BaseFragment
import com.bmprj.weatherforecast.ui.viewmodel.TodayViewModel
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@AndroidEntryPoint
class TodayFragment : BaseFragment<FragmentTodayBinding>(FragmentTodayBinding::inflate) {
    private val viewModel by viewModels<TodayViewModel> ()
    private val hourlyAdapter by lazy { HourlyAdapter() }
    private val rainyAdapter by lazy { RainyAdapter() }
    private val windAdapter by lazy { WindAdapter() }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun setUpViews() {


        binding.recyRain.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyRain.adapter=rainyAdapter

        binding.recyWind.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyWind.adapter=windAdapter

        binding.recy.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recy.adapter=hourlyAdapter


            val dh = DataBase.getInstance(requireContext())
            val search = DAO().get(dh)
            var city:String?=null

        if(DAO().get(dh).size==0|| DAO().get(dh).get(0).search==null || DAO().get(dh).get(0).search==getString(R.string.mevcutKonum)){
//            islocationenabled(view)

        }else{
            if(search.size>0){
                for(i in search) {
                    if(i.id==1){
                        city=i.search
                        getWeather(city)
                    }
                }
            }
        }


            binding.swipeRefreshLayout.setOnRefreshListener {
                binding.scrollV.visibility=View.GONE
                viewModel.refreshData(
                    "904aa43adf804caf913131326232306",
                    city,
                    3,
                    "no",
                    getString(R.string.lang)
                )
                binding.swipeRefreshLayout.isRefreshing=false
            }




        observeLiveData()
    }


    override fun onPause() {
        super.onPause()

        binding.animationView.resumeAnimation()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeLiveData(){

        viewModel.hourlyTod.observe(viewLifecycleOwner){ hourly ->
            hourly?.let{
                binding.scrollV.visibility=View.VISIBLE
                hourlyAdapter.updateList(hourly)
            }
        }

        viewModel.rainyTod.observe(viewLifecycleOwner) { rainy ->
            rainy?.let{
                rainyAdapter.updateList(rainy)
            }
        }
        viewModel.windyTod.observe(viewLifecycleOwner) { windy ->
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

        println(city)
        viewModel.refreshData(
            "904aa43adf804caf913131326232306",
            city,
            3,
            "no",
            getString(R.string.lang)
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingInflatedId", "InflateParams")
    fun islocationenabled(view:View){
        println("islocationenabled girdi")
        val v =layoutInflater.inflate(R.layout.alert_dialog_layout,null)
        val  alertDialog = AlertDialog.Builder(requireContext())
            .setView(v)
            .setCancelable(false)
            .create()

        alertDialog.setOnShowListener{
            val btnpoz = v.findViewById<Button>(R.id.tryyy)
            val btnneg = v.findViewById<Button>(R.id.searchcityy)
            btnpoz.setOnClickListener {
                println("tmm tikladi")
                requestPermissions(view)
                println(checkPermissions(view))
                if(checkPermissions(view)){
                    if(!(view.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        val vieww = layoutInflater.inflate(R.layout.location_is_of,null)
                        val  alert = AlertDialog.Builder(requireContext())
                            .setView(vieww)
                            .setCancelable(false)
                            .create()


                       val poz = vieww.findViewById<Button>(R.id.tryy)
                        val neg = vieww.findViewById<Button>(R.id.searchcity)

                        poz.setOnClickListener {
                            println("tamam tiklandi")
                            getLocation(view,alert)

                        }
                       neg.setOnClickListener {
                           println("sehir ara tiklandi")

                            Navigation.findNavController(binding.animationView).navigate(R.id.searchFragment)
                           alert.dismiss()
                        }

                        alert.show()
                        alertDialog.dismiss()
                    }else{
                        getLocation(view,alertDialog)
                    }
                }
            }

            btnneg.setOnClickListener {
                Navigation.findNavController(binding.animationView).navigate(R.id.searchFragment)
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission", "SuspiciousIndentation")
    fun getLocation(view:View,alertDialog: AlertDialog){

        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)

            mFusedLocationClient.lastLocation.addOnCompleteListener() { task ->
                val location: Location? = task.result
                if (location != null) {
                    println(location.latitude.toString()+","+location.longitude.toString())
//                    DAO().add(DatabaseHelper(requireContext()),1,"${location.latitude},${location.longitude}")
                    getWeather("${location.latitude},${location.longitude}")
                    alertDialog.dismiss()

                }
            }

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





