package com.bmprj.weatherforecast.adapter

import com.bmprj.weatherforecast.base.BaseAdapter
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.model.ThreeDay
import com.bmprj.weatherforecast.databinding.ThreeDaysLayoutBinding

class ThreeDayAdapter(
    override var list:List<ThreeDay>
    ) : BaseAdapter<ThreeDaysLayoutBinding, ThreeDay>(list){

    override val layoutId: Int = R.layout.three_days_layout

    override fun bind(binding: ThreeDaysLayoutBinding, item: ThreeDay) {
        binding.apply {
            threeday=item
            executePendingBindings()
        }
    }

}