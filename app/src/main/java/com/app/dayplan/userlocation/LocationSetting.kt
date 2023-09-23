package com.app.dayplan.userlocation

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.app.dayplan.util.SharedPreferencesHelper
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng

@Composable
fun LocationSettingASync() {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

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
}


@Composable
fun LocationSettingSync(onLocationReceived: (LatLng) -> Unit) {

    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                onLocationReceived(LatLng(it.latitude, it.longitude))
            }
        }
    }
}