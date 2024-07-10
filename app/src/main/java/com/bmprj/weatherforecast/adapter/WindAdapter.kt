package com.bmprj.weatherforecast.adapter

import com.bmprj.weatherforecast.base.BaseAdapter
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.model.Wind
import com.bmprj.weatherforecast.databinding.WindLayoutBinding
import com.bmprj.weatherforecast.util.setLayoutWidth

class WindAdapter()
    : BaseAdapter<WindLayoutBinding, Wind>(arrayListOf(), WindLayoutBinding::inflate){

    override fun bind(binding: WindLayoutBinding, item: Wind) {
        binding.apply {
            winddiw.rotation=item.wind_degree
            windd.text=item.wind
            img.setLayoutWidth(item.height)
            time.text=item.time
        }
    }


}