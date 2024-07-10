package com.bmprj.weatherforecast.adapter

import com.bmprj.weatherforecast.base.BaseAdapter
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.model.Rainy
import com.bmprj.weatherforecast.databinding.RainyLayoutBinding
import com.bmprj.weatherforecast.util.changeTextColor

class RainyAdapter(
    ) : BaseAdapter<RainyLayoutBinding, Rainy>(arrayListOf(),RainyLayoutBinding::inflate){


    override fun bind(binding: RainyLayoutBinding, item: Rainy) {
        binding.apply {
            percentageRainy.text=item.percentageRainy
            rating.rating=item.rating
            precipMm.text=item.precip_mm
            timeRainy.text=item.timeRainy
            precipMm.changeTextColor(item.rating)
        }
    }
}