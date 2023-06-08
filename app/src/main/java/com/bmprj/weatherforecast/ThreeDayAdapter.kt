package com.bmprj.weatherforecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bmprj.weatherforecast.databinding.ThreeDaysLayoutBinding

class ThreeDayAdapter(private val list:ArrayList<ThreeDay>)
    :RecyclerView.Adapter<ThreeDayAdapter.ViewHolder>(){

        class ViewHolder(private val binding:ThreeDaysLayoutBinding)
            :RecyclerView.ViewHolder(binding.root){

                fun bind(threeDay: ThreeDay){
                    binding.threeday=threeDay
                    binding.executePendingBindings()
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding:ThreeDaysLayoutBinding = ThreeDaysLayoutBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}