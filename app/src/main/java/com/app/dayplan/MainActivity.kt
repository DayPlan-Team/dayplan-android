package com.app.dayplan

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.app.dayplan.auth.LoginActivity
import com.app.dayplan.permission.RequestLocationPermissionActivity
import com.app.dayplan.userlocation.UpdateLocationWorker
import com.app.dayplan.util.GrantedLocation
import com.app.dayplan.util.SharedPreferencesHelper
import com.app.dayplan.util.startActivityAndFinish
import com.app.dayplan.verify.VerifyActivity
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (SharedPreferencesHelper.grantedLocation == GrantedLocation.DENIED.name) {
            this.startActivityAndFinish(RequestLocationPermissionActivity::class.java)
        } else {
            val accessToken = SharedPreferencesHelper.accessToken

            Log.i("accessToken = ", accessToken)

            if (accessToken.isNotEmpty()) {
                this.startActivityAndFinish(VerifyActivity::class.java)
            } else {
                this.startActivityAndFinish(LoginActivity::class.java)
            }
        }

        val periodicWorkRequest = PeriodicWorkRequestBuilder<UpdateLocationWorker>(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueue(periodicWorkRequest)

    }
}
