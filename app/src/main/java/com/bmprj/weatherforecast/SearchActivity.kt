package com.bmprj.weatherforecast

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bmprj.weatherforecast.Model.SearchV
import com.bmprj.weatherforecast.databinding.ActivitySearchBinding
import org.json.JSONArray
import java.net.URLDecoder
import java.net.URLEncoder

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
            val s = SearchV("Mevcut Konum","")
            list.add(s)
            adapter = SearchAdapter(list)
            binding.recyS.adapter=adapter
        }
    }

    override fun onStop() {
        super.onStop()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MainActivity::class.java))
    }


    fun backClick(view: View) {
        startActivity(Intent(this,MainActivity::class.java))

    }

    fun onQueryTextChange(query: String): Boolean {

        var str = ""
        if(query!=""){
            str = "https://api.weatherapi.com/v1/search.json?key=904aa43adf804caf913131326232306&q=${query}"

            val queue = Volley.newRequestQueue(this)
            val req = StringRequest(com.android.volley.Request.Method.GET,str,{
                response ->
                val json = URLDecoder.decode(URLEncoder.encode(response, "iso8859-1"),"UTF-8")

                val obj = JSONArray(json)

                val search = ArrayList<SearchV>()
                val f = SearchV("Mevcut Konum","")
                search.add(f)
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

            }, { })

            queue.add(req)

        }


        return false
    }
}