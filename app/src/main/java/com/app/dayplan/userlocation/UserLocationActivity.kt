package com.app.dayplan.userlocation

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewModelScope
import com.app.dayplan.datecourse.DateCourseSettingActivity
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.SharedPreferencesHelper
import com.app.dayplan.util.startActivityAndFinish
import kotlinx.coroutines.launch

class UserLocationActivity: ComponentActivity() {

    private val viewModel: LocationViewModel by viewModels()
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
        Log.i("get location = ", "location ...")

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            val locationListener = LocationListener { location ->
                SharedPreferencesHelper.latitude = location.latitude.toString()
                SharedPreferencesHelper.longitude = location.longitude.toString()

                val latitude = SharedPreferencesHelper.latitude.toDouble()
                val longitude = SharedPreferencesHelper.longitude.toDouble()

                viewModel.fetchCityCode(latitude, longitude)
            }

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                10.0F,
                locationListener
            )
        }
        // API 호출이 완료된 후에만 다음 액티비티로 이동
        this.startActivityAndFinish(DateCourseSettingActivity::class.java)
    }
}