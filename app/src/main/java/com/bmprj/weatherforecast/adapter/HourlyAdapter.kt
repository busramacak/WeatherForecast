package com.bmprj.weatherforecast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bmprj.weatherforecast.model.Hourly
import com.bmprj.weatherforecast.databinding.HourlyLayoutBinding

class HourlyAdapter(private val list:ArrayList<Hourly>)
    :RecyclerView.Adapter<HourlyAdapter.ViewHolder>(){

        class ViewHolder(private val binding:HourlyLayoutBinding)
            :RecyclerView.ViewHolder(binding.root){

                fun bind(hourly: Hourly?){
                    binding.hourly=hourly
                    binding.executePendingBindings()


                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val itemBinding:HourlyLayoutBinding= HourlyLayoutBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}