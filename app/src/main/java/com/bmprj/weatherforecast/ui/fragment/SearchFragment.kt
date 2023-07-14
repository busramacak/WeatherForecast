package com.bmprj.weatherforecast.ui.fragment

import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.ui.base.BaseFragment
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.SearchAdapter
import com.bmprj.weatherforecast.data.remote.ApiUtils
import com.bmprj.weatherforecast.databinding.FragmentSearchBinding
import com.bmprj.weatherforecast.data.model.SearchCity
import com.bmprj.weatherforecast.data.model.SearchCityItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {


    override fun setUpViews(view:View) {
        super.setUpViews(view)

        binding.search=this

        binding.recyS.apply {
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            binding.recyS.layoutManager=layoutManager
            val list = ArrayList<SearchCityItem>()
            val s = SearchCityItem("",0,0.0,0.0, getString(R.string.mevcutKonum),"","")
            list.add(s)
            adapter = SearchAdapter(list)
            binding.recyS.adapter=adapter
        }
    }

    fun onQueryTextChange(query: String): Boolean {

        val searchh = ArrayList<SearchCityItem>()
        val f = SearchCityItem("",0,0.0,0.0,getString(R.string.mevcutKonum),"","")
        searchh.add(f)

        if(query.length>0){

            val kdi = ApiUtils.getUrlInterface()
            kdi.getSearch("904aa43adf804caf913131326232306",query).enqueue(object : Callback<SearchCity>{
                override fun onResponse(call: Call<SearchCity>, response: Response<SearchCity>) {



                    for(i in 0 until response.body()!!.size){
                        val search = response.body()?.get(i)
                        val name = search?.name
                        val country = search?.country

                        val s = SearchCityItem( country,0,0.0,0.0,name,"","")
                        searchh.add(s)
                    }

                    binding.recyS.apply {
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.recyS.layoutManager = layoutManager
                        adapter = SearchAdapter(searchh)
                        binding.recyS.adapter = adapter
                    }



                }
                override fun onFailure(call: Call<SearchCity>, t: Throwable) {
                    Log.e("searchResponse",t.message.toString())
                }
            })


        }

        return false
    }


    fun backClick(view: View) {

        Navigation.findNavController(view).navigate(R.id.todayFragment)
    }

}