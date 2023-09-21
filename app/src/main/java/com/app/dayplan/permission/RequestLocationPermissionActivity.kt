package com.app.dayplan.permission

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.userlocation.UserLocationActivity
import com.app.dayplan.util.GrantedLocation
import com.app.dayplan.util.SharedPreferencesHelper
import com.app.dayplan.util.startActivityAndClearPrevious
import com.app.dayplan.util.startActivityAndFinish

class RequestLocationPermissionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DayplanTheme {
                RequestLocationPermission()
            }
        }
    }

    @Composable
    fun RequestLocationPermission() {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            when {
                fineLocationGranted -> {
                    SharedPreferencesHelper.saveLocationPermissionStatus(GrantedLocation.GRATED_FINE)
                }
                coarseLocationGranted -> {
                    SharedPreferencesHelper.saveLocationPermissionStatus(GrantedLocation.GRATED_COARSE)
                }
                else -> {
                    SharedPreferencesHelper.saveLocationPermissionStatus(GrantedLocation.DENIED)
                }
            }

            this.startActivityAndFinish(UserLocationActivity::class.java)
        }

        val requestPermission = {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        Button(
            onClick = requestPermission) {

            Text("Request Location Permission")
        }
    }
}