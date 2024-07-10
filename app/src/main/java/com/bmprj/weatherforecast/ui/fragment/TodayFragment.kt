package com.bmprj.weatherforecast.ui.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.BuildConfig
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.HourlyAdapter
import com.bmprj.weatherforecast.adapter.RainyAdapter
import com.bmprj.weatherforecast.adapter.WindAdapter
import com.bmprj.weatherforecast.databinding.FragmentTodayBinding
import com.bmprj.weatherforecast.base.BaseFragment
import com.bmprj.weatherforecast.ui.viewmodel.TodayViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class TodayFragment : BaseFragment<FragmentTodayBinding>(FragmentTodayBinding::inflate) {
    private val viewModel by viewModels<TodayViewModel> ()
    private val hourlyAdapter by lazy { HourlyAdapter() }
    private val rainyAdapter by lazy { RainyAdapter() }
    private val windAdapter by lazy { WindAdapter() }
    private val findNavController by lazy { findNavController() }
    private lateinit var alertDialog : AlertDialog
    private lateinit var alert : AlertDialog
    private var city:String?=null

    override fun setUpViews() {
        viewModel.getSearch()
        setUpAdapter()
        setUpListeners()
        setUpLiveDataObservers()
    }

    private fun setUpListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.scrollV.visibility=View.GONE
            viewModel.refreshData(
                BuildConfig.API_KEY,
                city,
                3,
                "no",
                getString(R.string.lang)
            )
            binding.swipeRefreshLayout.isRefreshing=false
        }
    }

    private fun setUpAdapter() {
        binding.recyRain.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyRain.adapter=rainyAdapter

        binding.recyWind.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyWind.adapter=windAdapter

        binding.recy.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recy.adapter=hourlyAdapter
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setUpLiveDataObservers(){
        viewModel.location.handleState(
            onSucces = {
                it?.let {
                    getWeather("${it.latitude},${it.longitude}")
                    alertDialog.dismiss()
                    alert.dismiss()
                }
            }
        )
        viewModel.search.handleState(
            onSucces = {
                if(it.isEmpty() || it[0].search==null || it[0].search==getString(R.string.mevcutKonum)){
                    isLocationEnabled(requireView())

                }else{
                    if(it.isNotEmpty()){
                        for(i in it) {
                            if(i.id==1){
                                city=i.search
                                getWeather(city)
                            }
                        }
                    }
                }
            }
        )
        viewModel.hourlyTod.handleState(
            onSucces = {
                binding.scrollV.visibility=View.VISIBLE
                hourlyAdapter.updateList(it)
            }
        )

        viewModel.rainyTod.handleState(
            onSucces = {
                rainyAdapter.updateList(it)
            }
        )
        viewModel.windyTod.handleState(
            onSucces = {
                windAdapter.updateList(it)
            }
        )

        val formatter = DateTimeFormatter.ofPattern("HH")
                val currentt = LocalDateTime.now().format(formatter)
                val t = currentt.toInt()

        viewModel.today.handleState(
            onSucces = { today ->
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
        )

    }

    private fun getWeather(city:String?){
        viewModel.refreshData(BuildConfig.API_KEY, city, 3, "no", getString(R.string.lang))
    }

    private fun isLocationEnabled(view:View){
        val v =layoutInflater.inflate(R.layout.alert_dialog_layout,null)
        alertDialog= AlertDialog.Builder(requireContext()).setView(v).setCancelable(false).create()

        alertDialog.setOnShowListener{
            val btnpoz = v.findViewById<Button>(R.id.tryyy)
            val btnneg = v.findViewById<Button>(R.id.searchcityy)
            btnpoz.setOnClickListener {

                requestPermissions(view)

                if(checkPermissions(view)){
                    if(!(requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        val vieww = layoutInflater.inflate(R.layout.location_is_of,null)
                        alert = AlertDialog.Builder(requireContext())
                            .setView(vieww)
                            .setCancelable(false)
                            .create()


                       val poz = vieww.findViewById<Button>(R.id.tryy)
                        val neg = vieww.findViewById<Button>(R.id.searchcity)

                        poz.setOnClickListener {
                            getLocation()
                        }
                       neg.setOnClickListener {
                           val action = TodayFragmentDirections.actionTodayFragmentToSearchFragment()
                           findNavController.navigate(action)
                           alert.dismiss()
                        }

                        alert.show()
                        alertDialog.dismiss()
                    }else{
                        getLocation()
                    }
                }
            }

            btnneg.setOnClickListener {
                val action = TodayFragmentDirections.actionTodayFragmentToSearchFragment()
                findNavController.navigate(action)
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }



    private fun getLocation(){
        viewModel.getLocation()
    }

    private fun checkPermissions(view:View): Boolean {
        return ActivityCompat.checkSelfPermission(
            view.context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    view.context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermissions(view:View) {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            2
        )
    }

    override fun onPause() {
        super.onPause()
        binding.animationView.resumeAnimation()
    }
}





