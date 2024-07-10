package com.bmprj.weatherforecast.adapter

import com.bmprj.weatherforecast.base.BaseAdapter
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.model.ThreeDay
import com.bmprj.weatherforecast.databinding.ThreeDaysLayoutBinding
import com.bmprj.weatherforecast.util.setImgUrl

class ThreeDayAdapter(
    ) : BaseAdapter<ThreeDaysLayoutBinding, ThreeDay>(arrayListOf(),ThreeDaysLayoutBinding::inflate){


    override fun bind(binding: ThreeDaysLayoutBinding, item: ThreeDay) {
        binding.apply {
            day.text=item.day
            condition.text=item.condition
            image.setImgUrl(item.image)
            maxTemp.text=item.maxTemp
            minTemp.text=item.minTemp
        }
    }

}