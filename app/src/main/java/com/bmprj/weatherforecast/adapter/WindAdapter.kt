package com.bmprj.weatherforecast.adapter

import com.bmprj.weatherforecast.ui.base.BaseAdapter
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.model.Wind
import com.bmprj.weatherforecast.databinding.WindLayoutBinding

class WindAdapter(override var list:List<Wind>)
    : BaseAdapter<WindLayoutBinding, Wind>(list){

    override val layoutId: Int = R.layout.wind_layout

    override fun bind(binding: WindLayoutBinding, item: Wind) {
        binding.apply {
            wind=item
            executePendingBindings()
        }
    }


}