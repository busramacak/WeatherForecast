package com.bmprj.weatherforecast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bmprj.weatherforecast.databinding.FragmentTomorrowBinding

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

}