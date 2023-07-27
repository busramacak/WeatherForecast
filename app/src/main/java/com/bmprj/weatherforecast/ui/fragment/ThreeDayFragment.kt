@file:Suppress("DEPRECATION")

package com.bmprj.weatherforecast.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bmprj.weatherforecast.ui.base.BaseFragment
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.ThreeDayAdapter
import com.bmprj.weatherforecast.data.remote.ApiUtils
import com.bmprj.weatherforecast.data.db.DAO
import com.bmprj.weatherforecast.data.db.DataBase
import com.bmprj.weatherforecast.databinding.FragmentThreeDayBinding
import com.bmprj.weatherforecast.data.model.ThreeDay
import com.bmprj.weatherforecast.data.model.Weather
import com.bmprj.weatherforecast.viewmodel.ThreeDaysViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class ThreeDayFragment : BaseFragment<FragmentThreeDayBinding>(R.layout.fragment_three_day) {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private lateinit var viewModel:ThreeDaysViewModel
    private val threedaysAdapter = ThreeDayAdapter(arrayListOf())

    override fun setUpViews(view:View) {
        super.setUpViews(view)

        binding.threeDay=this

        viewModel = ViewModelProviders.of(this@ThreeDayFragment)[ThreeDaysViewModel::class.java]

        binding.recyThreeDay.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.recyThreeDay.adapter=threedaysAdapter




        val dh = DataBase.getInstance(requireContext())
        val search = DAO().get(dh)
        var city:String?=null
        if(search.size>0){
            for(i in search){
                if(i.id==1){
                    city=i.search
                    getWeather(city)
                    break
                }
            }
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing=false
            binding.constrain.visibility=View.GONE
            getWeather(city)
        }

        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.threeDay.observe(viewLifecycleOwner, Observer { threeDay ->
            threeDay?.let{
                binding.constrain.visibility=View.VISIBLE
                threedaysAdapter.updateList(threeDay)
                binding.title.text= threeDay[0].cityName
            }
        })
    }

    private fun getWeather(city:String?){

        viewModel.refreshData("904aa43adf804caf913131326232306",city,3,"no",getString(R.string.lang))

    }
}