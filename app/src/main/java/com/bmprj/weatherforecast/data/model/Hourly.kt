package com.bmprj.weatherforecast.data.model

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


data class Hourly(val weatherimgHourly:String?,val time:String,val temp:String )
