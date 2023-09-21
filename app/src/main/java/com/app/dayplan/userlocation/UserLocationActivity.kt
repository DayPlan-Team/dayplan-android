package com.app.dayplan.userlocation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.app.dayplan.auth.LoginActivity
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.SharedPreferencesHelper
import com.app.dayplan.util.startActivityAndClearPrevious
import com.app.dayplan.util.startActivityAndFinish
import com.app.dayplan.verify.VerifyActivity
import com.google.android.gms.location.LocationServices

class UserLocationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DayplanTheme {
                GetLocation()
            }
        }

    }

    @Composable
    fun GetLocation() {
        val context = LocalContext.current
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        Log.i("get location = ", "location ...")

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LaunchedEffect(Unit) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        Log.d("Location", "Lat: ${it.latitude}, Lng: ${it.longitude}")
                        SharedPreferencesHelper.latitude =  it.latitude.toString()
                        SharedPreferencesHelper.longitude = it.longitude.toString()

                        Log.d("sharedLocation", "Lat: ${SharedPreferencesHelper.latitude}, Lng: ${SharedPreferencesHelper.longitude}")
                    }
                }.addOnFailureListener { exception ->
                    Log.e("LocationError", "Error fetching location: ${exception.message}")
                }
            }
        }

        val accessToken = SharedPreferencesHelper.accessToken

        Log.i("accessToken = ", accessToken)

        if (accessToken.isNotEmpty()) {
            this.startActivityAndFinish(VerifyActivity::class.java)
        } else {
            this.startActivityAndFinish(LoginActivity::class.java)
        }
    }
}
