package com.bmprj.weatherforecast.ui.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.base.BaseFragment
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.ThreeDayAdapter
import com.bmprj.weatherforecast.databinding.FragmentThreeDayBinding
import com.bmprj.weatherforecast.ui.viewmodel.ThreeDaysViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThreeDayFragment : BaseFragment<FragmentThreeDayBinding>(FragmentThreeDayBinding::inflate) {
    private val viewModel by viewModels<ThreeDaysViewModel>()
    private val threedaysAdapter by lazy { ThreeDayAdapter() }

    override fun setUpViews() {

        binding.recyThreeDay.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyThreeDay.adapter=threedaysAdapter

        viewModel.refreshData()



        binding.swipeRefreshLayout.setOnRefreshListener {

            binding.constrain.visibility=View.GONE
            viewModel.refreshData()
            binding.swipeRefreshLayout.isRefreshing=false
        }

        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.threeDay.observe(viewLifecycleOwner) { threeDay ->
            threeDay?.let {
                binding.constrain.visibility = View.VISIBLE
                threedaysAdapter.updateList(threeDay)
                binding.title.text = threeDay[0].cityName
            }
        }
    }

}