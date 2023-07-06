package com.bmprj.weatherforecast.ui

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.adapter.ThreeDayAdapter
import com.bmprj.weatherforecast.data.remote.ApiUtils
import com.bmprj.weatherforecast.data.db.DAO
import com.bmprj.weatherforecast.data.db.DatabaseHelper
import com.bmprj.weatherforecast.databinding.FragmentThreeDayBinding
import com.bmprj.weatherforecast.model.ThreeDay
import com.bmprj.weatherforecast.model.Weather
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

class ThreeDayFragment : Fragment() {

    private lateinit var binding: FragmentThreeDayBinding
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_three_day, container, false)
        binding.threeDay=this
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialog = ProgressDialog(context)
        dialog.setMessage("Yükleniyor...")
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.show()
        uiScope.launch(Dispatchers.Main) {
            val dh = DatabaseHelper(requireContext())
            val search = DAO().get(dh)
            var city:String? = null
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

    fun getWeather(city:String?,dialog: AlertDialog){

        val kdi = ApiUtils.getUrlInterface()
        kdi.getWeather("904aa43adf804caf913131326232306",city,3,"no","tr").enqueue(object :
            Callback<Weather>{
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {


                val forecastday = response.body()?.forecast?.forecastday



                val threeday = ArrayList<ThreeDay>()
                for(i in 0..forecastday!!.size-1){

                    val hour = forecastday?.get(i)

                    val day = hour?.day
                    val date = hour?.date

                    val inFormat = SimpleDateFormat("yyyy-MM-dd")
                    val dat: Date = inFormat.parse(date) as Date
                    val outFormatDays = SimpleDateFormat("EEEE")
                    val goal: String = outFormatDays.format(dat)
                    val outFormatMonth = SimpleDateFormat("MMM")
                    val month:String = outFormatMonth.format(dat)
                    val outFormatDay = SimpleDateFormat("dd")
                    val dy : String = outFormatDay.format(dat)

                    val max_temp = day?.maxtemp_c
                    val min_temp = day?.mintemp_c

                    val condition =day?.condition
                    val icon = condition?.icon
                    val conditionText = condition?.text

                    val t = ThreeDay(goal+", "+month+" "+dy,conditionText,max_temp.toString()+"°",min_temp.toString()+"°",icon)

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