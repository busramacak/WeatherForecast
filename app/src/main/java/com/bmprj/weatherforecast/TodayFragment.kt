package com.bmprj.weatherforecast

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.databinding.FragmentTodayBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class TodayFragment() : Fragment() {
    private lateinit var binding: FragmentTodayBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_today, container, false)
        binding.today=this



        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshCurrentClick(view:View){
        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        val r = RequestCurrent(view,mFusedLocationClient)


        r.getLocation(binding)

    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        val r = RequestCurrent(view,mFusedLocationClient)


        r.getLocation(binding)



    }









}