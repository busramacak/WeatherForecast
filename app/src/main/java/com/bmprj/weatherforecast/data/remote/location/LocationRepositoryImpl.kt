package com.bmprj.weatherforecast.data.remote.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import com.google.android.gms.location.FusedLocationProviderClient
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
) : LocationRepository{

    private lateinit var result: Location
    @SuppressLint("MissingPermission")
    override suspend fun getLocation(): Flow<Location?>  = flow {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            result = it
        }
        if(::result.isInitialized){
            emit(result)
        }

    }
}