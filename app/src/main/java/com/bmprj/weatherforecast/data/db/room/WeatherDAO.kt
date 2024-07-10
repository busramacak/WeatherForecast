package com.bmprj.weatherforecast.data.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bmprj.weatherforecast.model.Search
import com.bmprj.weatherforecast.model.Weather

@Dao
interface WeatherDAO {

    @Insert
    suspend fun insertAll(weather: Weather)


    @Query("SELECT * FROM weather ")
    suspend fun getWeather(): Weather


    @Query("DELETE FROM weather")
    suspend fun delete()


    @Insert
    suspend fun insertSearch(id:Int, search:String)

    @Update
    suspend fun updateSearch(search:Search)

    @Query("SELECT * FROM search")
    suspend fun getSearch():ArrayList<Search>



}