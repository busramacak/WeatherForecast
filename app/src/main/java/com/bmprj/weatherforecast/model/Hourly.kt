package com.bmprj.weatherforecast.model

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


data class Hourly(val weatherimgHourly:String?,val time:String,val temp:String )
@BindingAdapter("imageUrl")
fun setImageUrl(imgView: ImageView, imgUrl: String?){

    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}
