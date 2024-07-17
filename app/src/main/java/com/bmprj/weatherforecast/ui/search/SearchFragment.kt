package com.bmprj.weatherforecast.ui.search

import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.BuildConfig
import com.bmprj.weatherforecast.base.BaseFragment
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.databinding.FragmentSearchBinding
import com.bmprj.weatherforecast.model.Search
import com.bmprj.weatherforecast.model.SearchCityItem
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel by viewModels<SearchViewModel>()
    private val searchAdapter by lazy { SearchAdapter(::onCityClicked) }
    private val findNavController by lazy { findNavController() }
        override fun setUpViews() {
        setUpListeners()
        setUpAdapter()
        setUpLiveDataObservers()
    }

    private fun setUpAdapter() {
        val s = SearchCityItem("",0,0.0,0.0, getString(R.string.mevcutKonum),"","")

        val list = arrayListOf(s)
        searchAdapter.updateList(list)

        binding.searchRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.searchRecyclerView.adapter=searchAdapter

    }

    private fun setUpListeners() {
        with(binding) {
            searchView.setupWithSearchBar(binding.searchBar)
            searchView.editText.addTextChangedListener {
                viewModel.refreshData(BuildConfig.API_KEY,it.toString())
            }

            backButton.setOnClickListener { backButtonClicked() }
        }
    }


    private fun setUpLiveDataObservers(){
        viewModel.search.handleState(
            onLoading = {},
            onError = {
                      Log.e("tagotag",it.message.toString())
            },
            onSucces = {
                searchAdapter.updateList(it)
            }
        )

        viewModel.insertSearch.handleState(
            onLoading = {},
            onSucces = {}
        )

    }




    private fun backButtonClicked() {
        val action = SearchFragmentDirections.actionSearchFragmentToTodayFragment()
        findNavController.navigate(action)
    }

    private fun onCityClicked(item: SearchCityItem) {
       val  action= SearchFragmentDirections.actionSearchFragmentToTodayFragment()
        viewModel.inserttSearch(Search(id = 1,item.name!!))
        findNavController.navigate(action)
    }

}