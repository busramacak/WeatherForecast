package com.bmprj.weatherforecast.adapter

import com.bmprj.weatherforecast.base.BaseAdapter
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.model.Hourly
import com.bmprj.weatherforecast.databinding.HourlyLayoutBinding


class HourlyAdapter(
    override var list: List<Hourly>
    ) : BaseAdapter<HourlyLayoutBinding, Hourly>(list){

    override val layoutId:Int = R.layout.hourly_layout


    override fun bind(binding: HourlyLayoutBinding, item: Hourly) {
        binding.apply {
            hourly=item
            executePendingBindings()
        }
    }
}