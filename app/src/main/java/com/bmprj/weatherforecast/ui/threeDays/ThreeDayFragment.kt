package com.bmprj.weatherforecast.ui.threeDays

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.base.BaseFragment
import com.bmprj.weatherforecast.databinding.FragmentThreeDayBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThreeDayFragment : BaseFragment<FragmentThreeDayBinding>(FragmentThreeDayBinding::inflate) {
    private val viewModel by viewModels<ThreeDaysViewModel>()
    private val threeDaysAdapter by lazy { ThreeDayAdapter() }

    override fun setUpViews() {

        viewModel.refreshData()
        setUpAdapter()
        setUpLiveDataObservers()
    }


    private fun setUpAdapter() {
        binding.recyThreeDay.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyThreeDay.adapter=threeDaysAdapter
    }

    private fun setUpLiveDataObservers(){
        viewModel.threeDay.handleState(
           onSucces = {
               it.let {
                   binding.constrain.visibility = View.VISIBLE
                   threeDaysAdapter.updateList(it)
                   binding.title.text = it[0].cityName
               }
           }
        )
    }

}