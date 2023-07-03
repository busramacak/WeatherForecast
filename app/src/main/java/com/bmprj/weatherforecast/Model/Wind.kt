package com.bmprj.weatherforecast.Model

import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter




data class Wind(val wind: String,val height:Int,val wind_degree:Float, val time:String)

@BindingAdapter("height")
fun setLayoutWidth(view: ImageView, height: Int) {
    val layoutParams: ViewGroup.LayoutParams = view.layoutParams
    layoutParams.height = height
    view.setLayoutParams(layoutParams)
}