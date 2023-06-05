package com.bmprj.weatherforecast

import android.widget.ImageView
import androidx.databinding.BindingAdapter

data class Rainy(val percentageRainy:String,val timeRainy:String,val precip_mm:String,val rating:Float)

//@BindingAdapter("android:src")
//fun setImageDrawable(view: ImageView, drawable: Int) {
//    view.setImageResource(drawable)
//}
