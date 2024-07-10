package com.bmprj.weatherforecast.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searchh")
data class Search(
    @PrimaryKey(autoGenerate = false)
    val id:Int=0,
    val search:String)
{
}