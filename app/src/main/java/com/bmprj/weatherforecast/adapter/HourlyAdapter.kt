package com.bmprj.weatherforecast.adapter

import androidx.core.net.toUri
import com.bmprj.weatherforecast.base.BaseAdapter
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.model.Hourly
import com.bmprj.weatherforecast.databinding.HourlyLayoutBinding
import com.bmprj.weatherforecast.util.setImgUrl
import com.bumptech.glide.Glide


class HourlyAdapter(
) : BaseAdapter<HourlyLayoutBinding, Hourly>(arrayListOf(), HourlyLayoutBinding::inflate){

    override fun bind(binding: HourlyLayoutBinding, item: Hourly) {
        binding.apply {

            temp.text=item.temp
            time.text=item.time
            weatherimgHourly.setImgUrl(item.weatherimgHourly)
        }

    }




}