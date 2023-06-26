
package com.bmprj.weatherforecast

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bmprj.weatherforecast.databinding.FragmentTodayBinding
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*


class TodayFragment() : Fragment() {
    private lateinit var binding: FragmentTodayBinding
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_today, container, false)
        binding.today=this



        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshCurrentClick(view:View){
        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        val r = RequestCurrent(view,mFusedLocationClient)

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
                        r.getLocation(binding,dialog,city)
                        break
                    }
                }
            }else{
                r.getLocation(binding,dialog,city)
            }

        }



    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        val r = RequestCurrent(view,mFusedLocationClient)
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
                        r.getLocation(binding,dialog,city)

                        break
                    }
                }
            }else{
                r.getLocation(binding,dialog,city)
            }

        }




    }


}