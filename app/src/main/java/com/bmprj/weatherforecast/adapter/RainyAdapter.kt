package com.bmprj.weatherforecast.adapter

import com.bmprj.weatherforecast.ui.base.BaseAdapter
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.model.Rainy
import com.bmprj.weatherforecast.databinding.RainyLayoutBinding

class RainyAdapter(
    override var list:List<Rainy>
    ) : BaseAdapter<RainyLayoutBinding, Rainy>(list){

    override val layoutId: Int = R.layout.rainy_layout

    override fun bind(binding: RainyLayoutBinding, item: Rainy) {
        binding.apply {
            rainy=item
            executePendingBindings()
        }
    }
}