package com.bmprj.weatherforecast

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bmprj.weatherforecast.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.main=this



        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNav,navHostFragment.navController)

        val dh = DatabaseHelper(this)
        DAO().add(dh,1,null)

        val search = DAO().get(dh)

        for( i in search){
            if(i.id==1){
                binding.title.text=i.search
                break
            }

        }

    }





    fun searchClick(){

        startActivity(Intent(this,SearchActivity::class.java))

    }



}