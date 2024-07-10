package com.bmprj.weatherforecast.ui.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.base.BaseFragment
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.SearchAdapter
import com.bmprj.weatherforecast.databinding.FragmentSearchBinding
import com.bmprj.weatherforecast.model.SearchCityItem
import com.bmprj.weatherforecast.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel by viewModels<SearchViewModel>()
    private val searchAdapter by lazy { SearchAdapter(::onCityClicked) }

    override fun setUpViews() {
        setUpListeners()
        setUpAdapter()
        setUpLiveDataObservers()
    }

    private fun setUpAdapter() {
        val s = SearchCityItem("",0,0.0,0.0, getString(R.string.mevcutKonum),"","")

        var list = arrayListOf(s)
        searchAdapter.list=list

        binding.recyS.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyS.adapter=searchAdapter

    }

    private fun setUpListeners() {
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.refreshData("904aa43adf804caf913131326232306",newText.toString())
                return false
            }
        })
    }


    fun setUpLiveDataObservers(){

        viewModel.search.handleState(
            onLoading = {},
            onError = {},
            onSucces = {
                searchAdapter.updateList(it)
            }
        )

    }




    fun backClick(view: View) {

        Navigation.findNavController(view).navigate(R.id.todayFragment)
    }

    private fun onCityClicked(item: SearchCityItem) {

    }

}