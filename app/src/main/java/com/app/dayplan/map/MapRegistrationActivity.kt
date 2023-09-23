package com.app.dayplan.map

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.userlocation.LocationSettingSync
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap

class MapRegistrationActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DayplanTheme {
                UserStayedLocationScreen()
            }
        }
    }

    @Composable
    fun NaverMapView(
        onMapReady: (NaverMap) -> Unit
    ) {
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    onCreate(Bundle())
                    getMapAsync { naverMap -> onMapReady(naverMap) }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }

    @Composable
    fun UserStayedLocationScreen() {
        val naverMapState = remember { mutableStateOf<NaverMap?>(null) }
        val naverMap = naverMapState.value

        NaverMapView { map ->
            naverMapState.value = map
        }

        LocationSettingSync { latLng ->
            Log.i("lating = ", latLng.toString())
            Log.i("navermap is null = ", (naverMap == null).toString())

            naverMap?.moveCamera(CameraUpdate.scrollTo(latLng))
        }

    }
}