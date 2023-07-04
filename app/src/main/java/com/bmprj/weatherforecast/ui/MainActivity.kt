package com.bmprj.weatherforecast.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.view.View
import com.bmprj.weatherforecast.data.db.DatabaseHelper
import com.bmprj.weatherforecast.data.db.DAO
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bmprj.weatherforecast.R
import com.bmprj.weatherforecast.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main=this


        islocationenabled()


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNav,navHostFragment.navController)


        val dh = DatabaseHelper(this)

        if(DAO().get(dh).size==0){
            binding.title.text="Mevcut Konum"

        }else {
            val s = DAO().get(dh)
            for(i in s){
                binding.title.text=i.search
            }

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

            }
            alertDialog.setNegativeButton(Html.fromHtml("<font color='#757474'>HAYIR</font>"),null).create()
            alertDialog.create()
            alertDialog.show()
        }
    }





    fun searchClick(){

        startActivity(Intent(this,SearchActivity::class.java))

    }



}