package com.bmprj.weatherforecast.data.remote.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getLocation(): Flow<Location?>
}