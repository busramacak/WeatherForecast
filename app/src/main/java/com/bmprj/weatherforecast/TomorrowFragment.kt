package com.bmprj.weatherforecast

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.bmprj.weatherforecast.databinding.FragmentTomorrowBinding
import com.google.android.gms.location.LocationServices

class TomorrowFragment : Fragment() {
    private lateinit var binding: FragmentTomorrowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_tomorrow, container, false)
        binding.tomorrow=this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshCurrentClick(view:View){
        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        val r = RequestTomorrow(view,mFusedLocationClient)


        r.getLocation(binding)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        val r = RequestTomorrow(view,mFusedLocationClient)


        r.getLocation(binding)

    }
}