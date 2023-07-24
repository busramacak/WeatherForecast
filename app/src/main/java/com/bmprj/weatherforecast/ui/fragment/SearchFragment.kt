package com.bmprj.weatherforecast.ui.fragment

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.ui.base.BaseFragment
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.SearchAdapter
import com.bmprj.weatherforecast.data.remote.ApiUtils
import com.bmprj.weatherforecast.databinding.FragmentSearchBinding
import com.bmprj.weatherforecast.data.model.SearchCity
import com.bmprj.weatherforecast.data.model.SearchCityItem
import com.bmprj.weatherforecast.viewmodel.SearchViewModel
import com.bmprj.weatherforecast.viewmodel.TodayViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private lateinit var viewModel : SearchViewModel
    val list = ArrayList<SearchCityItem>()

    private lateinit var searchAdapter :SearchAdapter

    override fun setUpViews(view:View) {
        super.setUpViews(view)

        binding.search=this

        viewModel=ViewModelProviders.of(this@SearchFragment)[SearchViewModel::class.java]

        val s = SearchCityItem("",0,0.0,0.0, getString(R.string.mevcutKonum),"","")
        list.add(s)
        searchAdapter= SearchAdapter(list)

        binding.recyS.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.recyS.adapter=searchAdapter

        observeLiveData()
    }


    fun observeLiveData(){

        viewModel.search.observe(viewLifecycleOwner, Observer {search ->

            search?.let {
                searchAdapter.updateList(search)
            }

        })


    }

    fun onQueryTextChange(query: String): Boolean {

        viewModel.refreshData(requireContext(),"904aa43adf804caf913131326232306",query)

        if(query.length>0){

//            val kdi = ApiUtils.getUrlInterface()
//            kdi.getSearch("904aa43adf804caf913131326232306",query).enqueue(object : Callback<SearchCity>{
//                override fun onResponse(call: Call<SearchCity>, response: Response<SearchCity>) {
//
//
//
//                    for(i in 0 until response.body()!!.size){
//                        val search = response.body()?.get(i)
//                        val name = search?.name
//                        val country = search?.country
//
//                        val s = SearchCityItem( country,0,0.0,0.0,name,"","")
//                        searchh.add(s)
//                    }
//
//                    binding.recyS.apply {
//                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//                        binding.recyS.layoutManager = layoutManager
//                        adapter = SearchAdapter(searchh)
//                        binding.recyS.adapter = adapter
//                    }
//
//
//
//                }
//                override fun onFailure(call: Call<SearchCity>, t: Throwable) {
//                    Log.e("searchResponse",t.message.toString())
//                }
//            })


        }

        return false
    }


    fun backClick(view: View) {

        Navigation.findNavController(view).navigate(R.id.todayFragment)
    }

}