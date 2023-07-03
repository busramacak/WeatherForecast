package com.bmprj.weatherforecast

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
import com.bmprj.weatherforecast.adapter.HourlyAdapter
import com.bmprj.weatherforecast.adapter.RainyAdapter
import com.bmprj.weatherforecast.adapter.WindAdapter
import com.bmprj.weatherforecast.model.Hourly
import com.bmprj.weatherforecast.model.Rainy
import com.bmprj.weatherforecast.model.Weather
import com.bmprj.weatherforecast.model.Wind
import com.bmprj.weatherforecast.databinding.FragmentTomorrowBinding
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

@Suppress("DEPRECATION")
class TomorrowFragment : Fragment() {
    private lateinit var binding: FragmentTomorrowBinding
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main+job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_tomorrow, container, false)
        binding.tomorrow=this
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        val r = RequestTomorrow(view,mFusedLocationClient)


        val dialog = ProgressDialog(context)
        dialog.setMessage("Yükleniyor...")
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.show()

        uiScope.launch(Dispatchers.Main){
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
            }else{
//                r.getLocation(binding,dialog,city)
            }
        }


    }

    fun getWeather(city:String?,dialog: AlertDialog){

        val kdi = ApiUtils.getUrlInterface()
        kdi.getWeather("904aa43adf804caf913131326232306",city,2,"no","tr").enqueue(object :
            Callback<Weather> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {

                if(response!=null) {
                    Log.e("response",response.body().toString())
                    val current = response.body()?.current


                    val condition = current?.condition
                    val condition_text = condition?.text
                    val condition_code = condition?.code

                    val forecast = response.body()?.forecast
                    val hour = forecast?.forecastday?.get(1)?.hour
                    val date = forecast?.forecastday?.get(1)?.date
                    val day = forecast?.forecastday?.get(0)?.day
                    val max_temp = day?.maxtemp_c
                    val min_temp = day?.mintemp_c
                    val avghumidity = day?.avghumidity
                    val totalprecip_mm = day?.totalprecip_mm
                    val uv = day?.uv


                    val hourly = ArrayList<Hourly>()
                    val rainy = ArrayList<Rainy>()
                    val wind = ArrayList<Wind>()
                    var maxwind=0.0
                    var minwind=0.0

                    for(i in 0..hour!!.size-1){

                        val hourSet = hour.get(i)

                        val temp_hour = hourSet.temp_c
                        val cond_hour = hourSet.condition
                        val cond_icon = cond_hour.icon
                        val rain = hourSet.chance_of_rain
                        val precip_hour = hourSet.precip_mm
                        val wind_degr_hour = hourSet.wind_degree
                        val wind_kph_hour = hourSet.wind_kph

                        if(minwind>wind_kph_hour) minwind=wind_kph_hour

                        if(maxwind< wind_kph_hour) maxwind=wind_kph_hour

                        val r = Rainy("%"+ rain,i.toString()+":00", precip_hour.toString(),precip_hour.toFloat())
                        rainy.add(r)
                        val w = Wind(wind_kph_hour.toString(),wind_kph_hour.toInt()*3,wind_degr_hour.toFloat(),i.toString()+":00")
                        wind.add(w)
                        val h = Hourly(cond_icon,i.toString()+":00",temp_hour.toString()+"°")
                        hourly.add(h)

                    }



                    binding.recyWind.apply {
                        layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                        binding.recyWind.layoutManager=layoutManager
                        adapter= WindAdapter(wind)
                        binding.recyWind.adapter=adapter
                    }

                    binding.recyRain.apply {
                        layoutManager = LinearLayoutManager(context,
                            LinearLayoutManager.HORIZONTAL,false)
                        binding.recyRain.layoutManager=layoutManager
                        adapter= RainyAdapter(rainy)
                        binding.recyRain.adapter=adapter
                    }


                    binding.recy.apply {
                        layoutManager = LinearLayoutManager(context,
                            LinearLayoutManager.HORIZONTAL,false)
                        binding.recy.layoutManager=layoutManager
                        adapter= HourlyAdapter(hourly)
                        binding.recy.adapter=adapter
                    }

                    binding.date.text = date
                    binding.degree.text="Gündüz ${max_temp}, Gece ${min_temp}"
                    binding.condition.text=condition_text
                    binding.humidity.text="%"+avghumidity.toString()
                    binding.uv.text=uv.toString()
                    binding.totalprecip.text="Günlük Toplam hacim "+totalprecip_mm.toString()+" mm"
                    binding.windKph.text=minwind.toInt().toString()+"-"+maxwind.toInt().toString()

                    when(condition_code){
                        1000->{ binding.animationView.setAnimation(R.raw.sunny) }
                        1003->{ binding.animationView.setAnimation(R.raw.partly_cloudy) }
                        1006->{ binding.animationView.setAnimation(R.raw.cloudy) }
                        1030,1135,1147->{ binding.animationView.setAnimation(R.raw.mist) }
                        1114, 1117, 1204, 1207, 1213, 1219, 1225 -> {
                            binding.animationView.setAnimation(R.raw.snow)
                        }
                        1210,1216,1222,1249,1252,1255,1258 ->{
                            binding.animationView.setAnimation(R.raw.snow_sunny)
                        }
                        1087,1273,1276->{
                            binding.animationView.setAnimation(R.raw.thunder)

                        }
                        1183,1186,1189,1192,1195,1198,1201,1240,1243,1246->{
                            binding.animationView.setAnimation(R.raw.partly_shower)
                        }
                    }
                }

                dialog.dismiss()
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {

                Log.e("response",t.message.toString())
            }
        })

    }

    override fun onResume() {
        super.onResume()
        binding.animationView.playAnimation()
    }
}