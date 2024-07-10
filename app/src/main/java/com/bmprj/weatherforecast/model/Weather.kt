package com.bmprj.weatherforecast.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Entity(tableName = "weather")
@Serializable
data class Weather(
    @ColumnInfo(name = "current")
    @SerialName("current")
    val current: Current,
    @ColumnInfo(name = "forecast")
    @SerialName("forecast")
    val forecast: Forecast,
    @ColumnInfo(name = "location")
    @SerialName("location")
    val location: Location
){
    @PrimaryKey(autoGenerate = false)
    var uid :Int=0
}