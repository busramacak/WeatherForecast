package com.bmprj.weatherforecast.ui.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.ui.base.BaseFragment
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.SearchAdapter
import com.bmprj.weatherforecast.databinding.FragmentSearchBinding
import com.bmprj.weatherforecast.data.model.SearchCityItem
import com.bmprj.weatherforecast.viewmodel.SearchViewModel


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

        return false
    }


    fun backClick(view: View) {

        Navigation.findNavController(view).navigate(R.id.todayFragment)
    }

}