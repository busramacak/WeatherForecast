package com.bmprj.weatherforecast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bmprj.weatherforecast.model.Rainy
import com.bmprj.weatherforecast.databinding.RainyLayoutBinding

class RainyAdapter(private val list:ArrayList<Rainy>)
    :RecyclerView.Adapter<RainyAdapter.ViewHolder>(){

        class ViewHolder(private val binding:RainyLayoutBinding)
            :RecyclerView.ViewHolder(binding.root){

                fun bind(rainy: Rainy){
                    binding.rainy=rainy
                    binding.executePendingBindings()
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val itemBinding:RainyLayoutBinding= RainyLayoutBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}