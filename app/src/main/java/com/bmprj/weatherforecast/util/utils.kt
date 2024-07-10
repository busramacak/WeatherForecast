package com.bmprj.weatherforecast.util

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bmprj.weatherforecast.R
import com.bumptech.glide.Glide

fun TextView.changeTextColor(rating:Float){
    this.setTextColor(
        if(rating > 0 ){
        ContextCompat.getColor(this.context,R.color.rating)
    }else{
        ContextCompat.getColor(this.context,R.color.white)
    })
}

fun ImageView.setImgUrl(imgUrl: String?){
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(this.context)
            .load(imgUri)
            .into(this)
    }
}

fun ImageView.setLayoutWidth( height: Int) {
    val layoutParams: ViewGroup.LayoutParams = this.layoutParams
    layoutParams.height = height
    this.setLayoutParams(layoutParams)
}