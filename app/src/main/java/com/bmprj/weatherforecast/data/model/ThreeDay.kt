package com.bmprj.weatherforecast.data.model

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

data class ThreeDay(val cityName:String?,val day:String?, val condition:String?,val maxTemp:String,val minTemp:String, val image:String?)

@BindingAdapter("imageUrll")
fun setImageUrll(imgView: ImageView, imgUrl: String?){

    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}