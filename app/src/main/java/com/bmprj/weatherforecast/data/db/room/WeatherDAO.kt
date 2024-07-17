package com.bmprj.weatherforecast.data.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bmprj.weatherforecast.model.Search
import com.bmprj.weatherforecast.model.Weather

@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weather: Weather)

    @Query("SELECT * FROM weather")
    suspend fun getWeather(): Weather

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: Search)

    @Query("SELECT * FROM searchh WHERE id=:id")
    suspend fun getSearch(id:Int):Search?



}