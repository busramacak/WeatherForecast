package com.bmprj.weatherforecast.di

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.bmprj.weatherforecast.data.db.room.WeatherDAO
import com.bmprj.weatherforecast.data.db.room.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class DatabaseModule {

    @Provides
    @ViewModelScoped
    fun provideDatabase(@ApplicationContext context:Context) :WeatherDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            WeatherDatabase::class.java,
            "weatherdatabase")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @ViewModelScoped
    fun provideWeatherDao(db:WeatherDatabase):WeatherDAO = db.weatherDAO()
}