package com.bmprj.weatherforecast

import android.widget.ImageView
import androidx.databinding.BindingAdapter

data class Hourly(var weatherimgHourly:Int,var time:String )
@BindingAdapter("imageRes")
fun setImageDrawable(view: ImageView, drawable: Int) {
    view.setImageResource(drawable)
}
