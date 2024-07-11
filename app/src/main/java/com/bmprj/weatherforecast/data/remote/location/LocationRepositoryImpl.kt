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

    lateinit var result: Location
    override suspend fun getLocation(): Flow<Location?>  = flow {

        fusedLocationClient.lastLocation.addOnSuccessListener {
            result = it
        }
        if(::result.isInitialized){
            emit(result)
        }

    }


}