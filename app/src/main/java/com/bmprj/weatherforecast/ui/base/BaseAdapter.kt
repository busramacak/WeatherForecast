package com.bmprj.weatherforecast.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<DBinding:ViewDataBinding, T : Any>(open var list: List<T>)
    : RecyclerView.Adapter<BaseViewHolder<DBinding>>() {

    @get:LayoutRes
    abstract val layoutId:Int
    abstract fun bind(binding:DBinding,item:T)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DBinding> {
        val binder = DataBindingUtil.inflate<DBinding>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )

        return BaseViewHolder(binder)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BaseViewHolder<DBinding>, position: Int) {

        bind(holder.binder,list[position])
    }
}