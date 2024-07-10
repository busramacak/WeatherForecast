package com.bmprj.weatherforecast.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bmprj.weatherforecast.model.SearchCityItem

abstract class BaseAdapter<VBinding:ViewBinding, T : Any>(
    open var list: ArrayList<T> = arrayListOf(),
    private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VBinding
) : RecyclerView.Adapter<BaseViewHolder<VBinding>>() {

    abstract fun bind(binding:VBinding,item:T)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VBinding> {
        val binder = inflate(LayoutInflater.from(parent.context),parent,false)

        return BaseViewHolder(binder)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BaseViewHolder<VBinding>, position: Int) {
        bind(holder.binder,list[position])
    }


    fun updateList(newList: List<T>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }



}

