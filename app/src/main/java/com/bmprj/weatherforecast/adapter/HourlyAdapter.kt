package com.bmprj.weatherforecast.adapter

import com.bmprj.weatherforecast.ui.base.BaseAdapter
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.model.Hourly
import com.bmprj.weatherforecast.databinding.HourlyLayoutBinding


class HourlyAdapter(
    override var list: ArrayList<Hourly>
    ) : BaseAdapter<HourlyLayoutBinding, Hourly>(list){

    override val layoutId:Int = R.layout.hourly_layout


    override fun bind(binding: HourlyLayoutBinding, item: Hourly) {
        binding.apply {
            hourly=item
            executePendingBindings()
        }

    }




}