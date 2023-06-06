package com.bmprj.weatherforecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bmprj.weatherforecast.databinding.WindLayoutBinding

class WindAdapter(private val list:ArrayList<Wind>)
    :RecyclerView.Adapter<WindAdapter.ViewHolder>(){

        class ViewHolder(private val binding:WindLayoutBinding)
            :RecyclerView.ViewHolder(binding.root){

                fun bind(wind:Wind?){
                    binding.wind=wind
                    binding.executePendingBindings()
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val itemBinding:WindLayoutBinding= WindLayoutBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}