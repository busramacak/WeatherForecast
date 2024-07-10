package com.bmprj.weatherforecast.di

import com.bmprj.weatherforecast.BuildConfig
import com.bmprj.weatherforecast.data.remote.api.APIService
import com.bmprj.weatherforecast.data.remote.api.ApiRepository
import com.bmprj.weatherforecast.data.remote.api.ApiRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object ApiUtils{

    @Provides
    @ViewModelScoped
    fun provideApiUtils(api: APIService): ApiRepository = ApiRepositoryImpl(api)

    @Provides
    @ViewModelScoped
    fun provideApiService(): APIService {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }
}