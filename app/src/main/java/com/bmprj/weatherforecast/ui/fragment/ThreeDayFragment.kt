package com.bmprj.weatherforecast.ui.fragment

import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.ui.base.BaseFragment
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.ThreeDayAdapter
import com.bmprj.weatherforecast.databinding.FragmentThreeDayBinding
import com.bmprj.weatherforecast.ui.viewmodel.ThreeDaysViewModel
class ThreeDayFragment : BaseFragment<FragmentThreeDayBinding>(R.layout.fragment_three_day) {
    private lateinit var viewModel: ThreeDaysViewModel
    private val threedaysAdapter = ThreeDayAdapter(arrayListOf())

    override fun setUpViews(view:View) {
        super.setUpViews(view)

        binding.threeDay=this


        viewModel = ViewModelProviders.of(this@ThreeDayFragment)[ThreeDaysViewModel::class.java]

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