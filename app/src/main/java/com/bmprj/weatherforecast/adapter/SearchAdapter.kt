package com.bmprj.weatherforecast.adapter

import androidx.navigation.Navigation
import com.bmprj.weatherforecast.base.BaseAdapter
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.db.sqlite.DAO
import com.bmprj.weatherforecast.data.db.sqlite.DataBase
import com.bmprj.weatherforecast.databinding.SearchLayoutBinding
import com.bmprj.weatherforecast.model.SearchCityItem
import com.bmprj.weatherforecast.ui.fragment.SearchFragmentDirections


class SearchAdapter(
    private var onCityClicked:((SearchCityItem) -> Unit)
): BaseAdapter<SearchLayoutBinding, SearchCityItem>(arrayListOf(),SearchLayoutBinding::inflate) {


    override fun bind(binding: SearchLayoutBinding, item: SearchCityItem) {
        binding.apply {
            city.text=item.name
            country.text=item.country
            binding.constrain.setOnClickListener {
                onCityClicked.invoke(item)
//                DAO().delete(dh)
//                DAO().add(dh,1,binding.city.text.toString())
//
//
//
//                if(binding.city.text==binding.root.context.getString(R.string.mevcutKonum)){
//
//                    Navigation.findNavController(binding.root).navigate(gecis)
//
//                }else{
//                    Navigation.findNavController(binding.root).navigate(gecis)
//                }
            }
        }


    }

}

