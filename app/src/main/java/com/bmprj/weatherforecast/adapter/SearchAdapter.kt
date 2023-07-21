@file:Suppress("DEPRECATION")

package com.bmprj.weatherforecast.adapter

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import com.bmprj.weatherforecast.ui.base.BaseAdapter
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.db.DAO
import com.bmprj.weatherforecast.data.db.DataBase
import com.bmprj.weatherforecast.databinding.SearchLayoutBinding
import com.bmprj.weatherforecast.data.model.SearchCityItem
import com.bmprj.weatherforecast.ui.fragment.SearchFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
class SearchAdapter(override var list:ArrayList<SearchCityItem>)
    : BaseAdapter<SearchLayoutBinding, SearchCityItem>(list) {

    override val layoutId: Int = R.layout.search_layout

    override fun bind(binding: SearchLayoutBinding, item: SearchCityItem) {
        binding.apply {
            searchV=item
            executePendingBindings()
        }

        val dh = DataBase.getInstance(binding.root.context)
        val gecis = SearchFragmentDirections.actionSearchFragmentToTodayFragment()

        binding.constrain.setOnClickListener {
            if(DAO().get(dh).size==0){
                DAO().add(dh,1,binding.city.text.toString())

            }else
            {
                DAO().update(dh,1,binding.city.text.toString())
            }

            if(binding.city.text==binding.root.context.getString(R.string.mevcutKonum)){


                val locationManager: LocationManager =
                    it.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


                if (!( locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))){

                    val  alertDialog = AlertDialog.Builder(it.context)
                    val customLayout: View = LayoutInflater.from(it.context).inflate(R.layout.alert_dialog_layout, null)
                    alertDialog.setView(customLayout)



                    alertDialog.setPositiveButton(Html.fromHtml("<font color='#757474'>"+binding.root.context.getString(R.string.openSettings)+"</font>"))
                    { DialogInterface, which:Int ->

                        DAO().delete(dh)
                        it.context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

                        CoroutineScope(Dispatchers.Main).launch {

                            delay(5000)
                            Navigation.findNavController(binding.root).navigate(gecis)
                        }


                    }

                    alertDialog.create()
                    alertDialog.show()


                }else{
                    if (!(ActivityCompat.checkSelfPermission(
                            binding.root.context,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(
                                    binding.root.context,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED)
                    ){
                        CoroutineScope(Dispatchers.Main).launch{

                            DAO().delete(dh)

                            binding.root.context.startActivity(
                                Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:"+binding.root.context.packageName))
                            )
                            delay(5000)
                            Navigation.findNavController(binding.root).navigate(gecis)

//                                    Toast.makeText(itemView.context,getString(R.string.requestPermission))

                        }
                    }else{

                        CoroutineScope(Dispatchers.Main).launch {
                            val dialog = ProgressDialog(binding.root.context)
                            dialog.setMessage(binding.root.context.getString(R.string.yukleniyor))
                            dialog.setCancelable(false)
                            dialog.setInverseBackgroundForced(false)
                            dialog.show()
                            delay(1000)
                            Navigation.findNavController(binding.root).navigate(gecis)
                            dialog.dismiss()

                        }
                    }

                }




            }else{

                CoroutineScope(Dispatchers.Main).launch{
                    val dialog = ProgressDialog(binding.root.context)
                    dialog.setMessage(binding.root.context.getString(R.string.yukleniyor))
                    dialog.setCancelable(false)
                    dialog.setInverseBackgroundForced(false)
                    dialog.show()
                    delay(1000)

                    Navigation.findNavController(binding.root).navigate(gecis)

                    dialog.dismiss()
                }

            }

        }


    }

}

