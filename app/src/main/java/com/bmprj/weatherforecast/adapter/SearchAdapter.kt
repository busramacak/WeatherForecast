package com.bmprj.weatherforecast.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bmprj.weatherforecast.DAO
import com.bmprj.weatherforecast.DatabaseHelper
import com.bmprj.weatherforecast.MainActivity
import com.bmprj.weatherforecast.model.SearchV
import com.bmprj.weatherforecast.databinding.SearchLayoutBinding

class SearchAdapter(private val list:ArrayList<SearchV>)
    :RecyclerView.Adapter<SearchAdapter.ViewHolder>() {


        class ViewHolder(private val binding: SearchLayoutBinding)
            :RecyclerView.ViewHolder(binding.root){
            private lateinit var context: Context
                fun bind(searchV: SearchV?){
                    binding.searchV=searchV
                    binding.executePendingBindings()

                    context = itemView.context
                    val intent = Intent(context, MainActivity::class.java)
                    val dh = DatabaseHelper(binding.root.context)

                    binding.constrain.setOnClickListener {
                        if(DAO().get(dh).size==0){
                            DAO().add(dh,1,binding.city.text.toString())

                        }else
                        {
                            DAO().update(dh,1,binding.city.text.toString())
                        }

                        context.startActivity(intent)



                    }

                }

            }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val itemBinding: SearchLayoutBinding = SearchLayoutBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

}