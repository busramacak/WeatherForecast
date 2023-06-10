package com.bmprj.weatherforecast

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bmprj.weatherforecast.databinding.SearchLayoutBinding

class SearchAdapter(private val list:ArrayList<SearchV>)
    :RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

        class ViewHolder(private val binding: SearchLayoutBinding)
            :RecyclerView.ViewHolder(binding.root){

                fun bind(searchV: SearchV?){
                    binding.searchV=searchV
                    binding.executePendingBindings()

                    val dh = DatabaseHelper(binding.root.context)

                    binding.constrain.setOnClickListener {

                        DAO().add(dh,1,binding.city.text.toString())

                    }

                }

            }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
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