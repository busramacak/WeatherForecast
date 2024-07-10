package com.bmprj.weatherforecast.data.remote.location

import com.google.android.gms.location.FusedLocationProviderClient
import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationRepository{
    override suspend fun getLocation(): Flow<Location?>  = callbackFlow{
        fusedLocationClient.lastLocation.addOnSuccessListener {
            trySend(it)
        }.addOnFailureListener{
            trySend(null)
        }
    }


}