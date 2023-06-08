package com.bmprj.weatherforecast

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bmprj.weatherforecast.databinding.FragmentTodayBinding
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import okhttp3.internal.wait
import java.util.*


@Suppress("DEPRECATION")
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

        val dialog = ProgressDialog(context)
        dialog.setMessage("Yükleniyor...")
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.show()
        r.getLocation(binding,dialog)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        val r = RequestCurrent(view,mFusedLocationClient)


        val dialog = ProgressDialog(context)
        dialog.setMessage("Yükleniyor...")
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.show()
        r.getLocation(binding, dialog)
    }

    override fun onResume() {
        super.onResume()
        binding.animationView.playAnimation()
    }









}