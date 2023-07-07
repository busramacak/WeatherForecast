package com.bmprj.weatherforecast.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.provider.Settings
import android.text.Html
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.data.db.DAO
import com.bmprj.weatherforecast.data.db.DatabaseHelper
import com.bmprj.weatherforecast.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main=this





        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNav,navHostFragment.navController)


        navHostFragment.navController.addOnDestinationChangedListener{ _, nd: NavDestination, _ ->
            if( nd.id== R.id.searchFragment ){
                binding.bottomNav.visibility= View.GONE



            }else{
                binding.bottomNav.visibility= View.VISIBLE
            }

        }
        val dh = DatabaseHelper(this)

        if(DAO().get(dh).size==0 || DAO().get(dh).get(0).search=="Mevcut Konum"){
            islocationenabled()
        }

    }


    fun isLocationEnabled(): Boolean {

        val locationManager: LocationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingInflatedId")
    fun islocationenabled(){

        if (!isLocationEnabled()){

           val  alertDialog = AlertDialog.Builder(this)
            val customLayout: View = layoutInflater.inflate(R.layout.alert_dialog_layout, null)
            alertDialog.setView(customLayout)

            alertDialog.setPositiveButton(Html.fromHtml("<font color='#757474'>AYARLARI AÇ</font>")){ DialogInterface,which:Int ->
                this.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                CoroutineScope(Dispatchers.Main).launch {
                    delay(5000)
                    Navigation.findNavController(binding.navHostFragment).navigate(R.id.todayFragment)
                }

            }
            alertDialog.setNegativeButton(Html.fromHtml("<font color='#757474'>HAYIR</font>")) { DialogInterface, which: Int ->
                Navigation.findNavController(binding.navHostFragment).navigate(R.id.searchFragment)
            }
            alertDialog.create()
            alertDialog.show()
        }
    }



//    fun searchClick(){
//
//        binding.bottomNav.visibility=View.GONE
//
//        supportFragmentManager.beginTransaction().add(R.id.constrainMain, SearchFragment()).commit()
//
//    }



}