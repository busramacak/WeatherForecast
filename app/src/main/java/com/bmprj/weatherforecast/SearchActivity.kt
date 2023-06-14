package com.bmprj.weatherforecast

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.weatherforecast.databinding.ActivitySearchBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class SearchActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_search)
        binding.search=this

        binding.recyS.apply {
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            binding.recyS.layoutManager=layoutManager
            val list = ArrayList<SearchV>()
            val s = SearchV("Konumunuz","")
            list.add(s)
            adapter = SearchAdapter(list)
            binding.recyS.adapter=adapter
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MainActivity::class.java))
    }


    fun backClick(view: View) {
        startActivity(Intent(this,MainActivity::class.java))

    }

    fun onQueryTextSubmit(query: String): Boolean {

        var str = ""
        if(query!=""){
            str = "https://api.weatherapi.com/v1/search.json?key=3d94ea89afba4d1b8bf85744232605&q=${query}"

            val SDK_INT = Build.VERSION.SDK_INT
            if (SDK_INT > 8) {
                val policy = StrictMode.ThreadPolicy.Builder()
                    .permitAll().build()
                StrictMode.setThreadPolicy(policy)


                var client = OkHttpClient()
                val request = Request.Builder().url(str).build()
                val response = client.newCall(request).execute()

                val json = response.body!!.string()
                val obj = JSONArray(json)

                val search = ArrayList<SearchV>()
                for (i in 0 until obj.length()) {
                    val city = obj.getJSONObject(i)
                    val name = city.getString("name")
                    val country = city.getString("country")

                    val s = SearchV(name, country)
                    search.add(s)

                }

                binding.recyS.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    binding.recyS.layoutManager = layoutManager
                    adapter = SearchAdapter(search)
                    binding.recyS.adapter = adapter
                }




            }
        }

        return false
    }
}