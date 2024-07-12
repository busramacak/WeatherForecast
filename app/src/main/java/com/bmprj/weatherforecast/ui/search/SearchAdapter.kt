package com.bmprj.weatherforecast.ui.search

import com.bmprj.weatherforecast.base.BaseAdapter
import com.bmprj.weatherforecast.databinding.SearchLayoutBinding
import com.bmprj.weatherforecast.model.SearchCityItem


class SearchAdapter(
    private var onCityClicked:((SearchCityItem) -> Unit)
): BaseAdapter<SearchLayoutBinding, SearchCityItem>(arrayListOf(),SearchLayoutBinding::inflate) {


    override fun bind(binding: SearchLayoutBinding, item: SearchCityItem) {
        binding.apply {
            city.text=item.name
            country.text=item.country
            binding.constrain.setOnClickListener {
                onCityClicked.invoke(item)
            }
        }


    }

}

