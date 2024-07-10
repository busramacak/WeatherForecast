package com.bmprj.weatherforecast.ui.fragment

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.base.BaseFragment
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.HourlyAdapter
import com.bmprj.weatherforecast.adapter.RainyAdapter
import com.bmprj.weatherforecast.adapter.WindAdapter
import com.bmprj.weatherforecast.databinding.FragmentTomorrowBinding
import com.bmprj.weatherforecast.ui.viewmodel.TomorrowViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class TomorrowFragment : BaseFragment<FragmentTomorrowBinding>(FragmentTomorrowBinding::inflate) {
    private val viewModel by viewModels<TomorrowViewModel> ()
    private val hourlyAdapter = HourlyAdapter(arrayListOf())
    private val rainyAdapter = RainyAdapter(arrayListOf())
    private val windAdapter = WindAdapter(arrayListOf())

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setUpViews() {

        binding.recyRain.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyRain.adapter=rainyAdapter

        binding.recyWind.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyWind.adapter=windAdapter

        binding.recy.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recy.adapter=hourlyAdapter


        viewModel.refreshData()



        binding.swipeRefreshLayout.setOnRefreshListener {

            binding.scrollV.visibility=View.GONE
            viewModel.refreshData()
            binding.swipeRefreshLayout.isRefreshing=false
        }





        observeLiveData()
    }

    override fun onPause() {
        super.onPause()
        binding.animationView.playAnimation()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeLiveData(){

        viewModel.hourlyTom.observe(viewLifecycleOwner) { houryl ->
            houryl?.let {
                binding.scrollV.visibility=View.VISIBLE
                hourlyAdapter.updateList(it)
            }

        }

        viewModel.rainyTom.observe(viewLifecycleOwner) { rainy->
            rainy?.let {
                rainyAdapter.updateList(it)
            }
        }

        viewModel.windyTom.observe(viewLifecycleOwner)  { windy ->
            windy?.let { windAdapter.updateList(it) }

        }

        val formatter = DateTimeFormatter.ofPattern("HH")
        val currentt = LocalDateTime.now().format(formatter)
        val t = currentt.toInt()

        viewModel.tomorrow.observe(viewLifecycleOwner) { tomorrow ->
            tomorrow?.let {
                binding.title.text = it.cityname
                binding.date.text = it.date
                binding.degree.text = it.degree
                binding.condition.text = it.conditionText
                binding.humidity.text = it.humidity
                binding.uv.text = it.uv
                binding.totalprecip.text = it.totalPrecip
                binding.windKph.text = it.wind_kph


                when (tomorrow.code) {
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

                    1183, 1186, 1189, 1192, 1195, 1198, 1201, 1240, 1243, 1246, 1063 -> {
                        binding.animationView.setAnimation(R.raw.partly_shower)
                    }
                }

            }
        }
    }


}