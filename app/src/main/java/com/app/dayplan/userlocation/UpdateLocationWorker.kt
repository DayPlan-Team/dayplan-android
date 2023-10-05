package com.app.dayplan.userlocation

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.app.dayplan.api.auth.ApiAuthClient
import com.app.dayplan.util.SharedPreferencesHelper

class UpdateLocationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val latitude = SharedPreferencesHelper.latitude.toDouble()
        val longitude = SharedPreferencesHelper.longitude.toDouble()
        Log.i("latitude = ", latitude.toString())
        Log.i("longitude = ", longitude.toString())
        val response = ApiAuthClient.locationGeocodeService.updateUserLocation(
            Coordinates(
                latitude = latitude,
                longitude = longitude,
            )
        )
        return if (response.isSuccessful) Result.success() else Result.failure()
    }
}
