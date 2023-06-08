package com.bmprj.weatherforecast

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.bmprj.weatherforecast.databinding.FragmentThreeDayBinding
import com.google.android.gms.location.LocationServices

class ThreeDayFragment : Fragment() {

    private lateinit var binding: FragmentThreeDayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_three_day, container, false)
        binding.threeDay=this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        val r = RequestThreeDay(view,mFusedLocationClient)


        val dialog = ProgressDialog(context)
        dialog.setMessage("Yükleniyor...")
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.show()
        r.getLocation(binding,dialog)


    }


}