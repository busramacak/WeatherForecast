@file:Suppress("DEPRECATION")

package com.bmprj.weatherforecast.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.ui.base.BaseFragment
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.ThreeDayAdapter
import com.bmprj.weatherforecast.data.remote.ApiUtils
import com.bmprj.weatherforecast.data.db.DAO
import com.bmprj.weatherforecast.data.db.DataBase
import com.bmprj.weatherforecast.databinding.FragmentThreeDayBinding
import com.bmprj.weatherforecast.data.model.ThreeDay
import com.bmprj.weatherforecast.data.model.Weather
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

    override fun setUpViews(view:View) {
        super.setUpViews(view)

        binding.threeDay=this

        val dialog = ProgressDialog(context)
        dialog.setMessage(getString(R.string.yukleniyor))
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.show()
        uiScope.launch(Dispatchers.Main) {
            val dh = DataBase.getInstance(requireContext())
            val search = DAO().get(dh)
            val city:String?
            if(search.size>0){
                for(i in search){
                    if(i.id==1){
                        city=i.search
                        getWeather(city,dialog)
                        break
                    }
                }
            }
        }
    }

    private fun getWeather(city:String?,dialog: AlertDialog){

        val kdi = ApiUtils.getUrlInterface()
        kdi.getWeather("904aa43adf804caf913131326232306",city,3,"no",getString(R.string.lang)).enqueue(object :
            Callback<Weather>{
            @SuppressLint("SimpleDateFormat")
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

                val forecastday = response.body()?.forecast?.forecastday

                val cityName = response.body()?.location?.name
                binding.title.text=cityName

                val threeday = ArrayList<ThreeDay>()
                for(i in 0 until forecastday!!.size){

                    val hour = forecastday[i]

                    val day = hour.day
                    val date = hour.date

                    val inFormat = SimpleDateFormat("yyyy-MM-dd")
                    val dat: Date = inFormat.parse(date) as Date
                    val outFormatDays = SimpleDateFormat("EEEE")
                    val goal: String = outFormatDays.format(dat)
                    val outFormatMonth = SimpleDateFormat("MMM")
                    val month:String = outFormatMonth.format(dat)
                    val outFormatDay = SimpleDateFormat("dd")
                    val dy : String = outFormatDay.format(dat)

                    val max_temp = day.maxtemp_c
                    val min_temp = day.mintemp_c

                    val condition =day.condition
                    val icon = condition.icon
                    val conditionText = condition.text

                    val t = ThreeDay(getString(R.string.day_month_dy,goal,month,dy),conditionText,
                        getString(R.string.degre,max_temp.toString()),getString(R.string.degre, min_temp.toString()),icon)

                    threeday.add(t)
                }
                dialog.dismiss()

                binding.title.text=city

                binding.recyThreeDay.apply {
                    layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                    binding.recyThreeDay.layoutManager=layoutManager
                    adapter = ThreeDayAdapter(threeday)
                    binding.recyThreeDay.adapter=adapter
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Log.e("threeday","Erroroorroor")
            }
        })
    }
}