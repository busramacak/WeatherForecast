package com.bmprj.weatherforecast.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.model.SearchCity
import com.bmprj.weatherforecast.model.SearchCityItem
import com.bmprj.weatherforecast.di.ApiUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(

): ViewModel() {

    private val searchUtils = ApiUtils()

    val search = MutableLiveData<ArrayList<SearchCityItem>>()

    fun refreshData(context:Context,key:String,query:String){

        getDataFromApi(context,key, query)


    }

    private fun getDataFromApi(context: Context, key:String, query:String){
        val searchh = ArrayList<SearchCityItem>()

        val s = SearchCityItem("",0,0.0,0.0, context.getString(R.string.mevcutKonum),"","")

        searchh.add(s)

        if(query.isNotEmpty()){

            searchUtils.getSearch(key, query).enqueue(object : Callback<SearchCity> {
                override fun onResponse(call: Call<SearchCity>, response: Response<SearchCity>) {
                    for(i in 0 until response.body()!!.size){
                        val searc = response.body()?.get(i)
                        val name = searc?.name
                        val country = searc?.country

                        val d = SearchCityItem( country,0,0.0,0.0,name,"","")
                        searchh.add(d)
                    }

                    search.value=searchh

                }

                override fun onFailure(call: Call<SearchCity>, t: Throwable) {
                    t.printStackTrace()
                }

            })
        }

    }
}