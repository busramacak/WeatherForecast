package com.bmprj.weatherforecast.adapter

import androidx.navigation.Navigation
import com.bmprj.weatherforecast.ui.base.BaseAdapter
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.db.sqlite.DAO
import com.bmprj.weatherforecast.data.db.sqlite.DataBase
import com.bmprj.weatherforecast.databinding.SearchLayoutBinding
import com.bmprj.weatherforecast.data.model.SearchCityItem
import com.bmprj.weatherforecast.ui.fragment.SearchFragmentDirections


class SearchAdapter(override var list:ArrayList<SearchCityItem>)
    : BaseAdapter<SearchLayoutBinding, SearchCityItem>(list) {

    override val layoutId: Int = R.layout.search_layout

    override fun bind(binding: SearchLayoutBinding, item: SearchCityItem) {
        binding.apply {
            searchV=item
            executePendingBindings()
        }

        val dh = DataBase.getInstance(binding.root.context)
        val gecis = SearchFragmentDirections.actionSearchFragmentToTodayFragment()

        binding.constrain.setOnClickListener {
            DAO().delete(dh)
            DAO().add(dh,1,binding.city.text.toString())



            if(binding.city.text==binding.root.context.getString(R.string.mevcutKonum)){

                Navigation.findNavController(binding.root).navigate(gecis)

            }else{
                Navigation.findNavController(binding.root).navigate(gecis)
            }
        }
    }

}

