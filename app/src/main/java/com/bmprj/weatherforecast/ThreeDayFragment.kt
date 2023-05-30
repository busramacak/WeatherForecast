package com.bmprj.weatherforecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bmprj.weatherforecast.databinding.FragmentThreeDayBinding

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


}