package com.bmprj.weatherforecast.base


import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseViewHolder<BINDING : ViewBinding>(val binder: BINDING) :
    RecyclerView.ViewHolder(binder.root) {
}