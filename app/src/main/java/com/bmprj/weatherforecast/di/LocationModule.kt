package com.bmprj.weatherforecast.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object LocationModule {

    @Provides
    @ViewModelScoped
    fun provideLocationProviderClient(@ApplicationContext context: Context) : FusedLocationProviderClient{
        return LocationServices.getFusedLocationProviderClient(context)
    }
}