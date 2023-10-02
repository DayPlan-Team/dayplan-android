package com.app.dayplan.step

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.app.dayplan.api.auth.ApiAuthClient
import com.app.dayplan.datecourse.Location
import com.app.dayplan.home.HomeBar
import com.app.dayplan.home.TopBar
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.userlocation.LocationSettingSync
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Tm128
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import kotlinx.coroutines.launch
import java.lang.StringBuilder

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class StepLocationActivity : ComponentActivity() {

    private val selectedCityCode: Long by lazy {
        intent.getLongExtra("cityCode", Location.DEFAULT_CITY_CODE)
    }

    private val selectedCityName: String by lazy {
        intent.getStringExtra("cityName") ?: Location.DEFAULT_CITY_NAME
    }

    private val selectedDistrictCode: Long by lazy {
        intent.getLongExtra("districtCode", Location.DEFAULT_DISTRICT_CODE)
    }

    private val selectedDistrictName: String by lazy {
        intent.getStringExtra("districtName") ?: Location.DEFAULT_DISTRICT_NAME
    }

    private val currentCategoryNumber: Int by lazy {
        intent.getIntExtra("currentCategoryNumber", 0)
    }

    private val stepArray: ArrayList<Steps> by lazy {
        intent.getSerializableExtra("steps", ArrayList::class.java) as? ArrayList<Steps> ?: arrayListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DayplanTheme {
                StepScreen()
            }
        }
    }

    @Composable
    fun StepScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar()
            StepSection()
            Divider(color = Color.Gray, thickness = 3.dp)
            DateCourseCategoryThemeBox("원하는 위치를 선택 해주세요")
            UserStayedLocationScreen()
            HomeBar(this@StepLocationActivity)
        }
    }


    @Composable
    fun StepSection() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "데이트 코스 선택",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 16.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 16.dp)
            ) {
                WhereGoBox("step1")
                WhereGoPlusBox()
            }
        }
    }

    @Composable
    fun WhereGoBox(text: String) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 6.dp, bottom = 6.dp)
        ) {
            Text(text = text) // 이 텍스트는 박스 위에 나타납니다.
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) // 이것은 동그란 박스입니다.
        }
    }

    @Composable
    @Preview
    fun WhereGoPlusBox() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentSize()
        ) {
            Text(text = "") // 이 텍스트는 박스 위에 나타납니다.
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center  // 여기에 이 코드를 추가합니다.
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
    @Composable
    fun DateCourseCategoryThemeBox(
        text1: String,
    ) {
        Text(
            text = text1,
            style = TextStyle(fontWeight = FontWeight.Bold),
        )
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
            modifier = Modifier
                .height(300.dp)
        )
    }

    @Composable
    fun UserStayedLocationScreen() {
        val naverMapState = remember { mutableStateOf<NaverMap?>(null) }
        val coroutineScope = rememberCoroutineScope()

        NaverMapView { map ->
            naverMapState.value = map
            map.moveCamera(CameraUpdate.zoomTo(12.0))

            coroutineScope.launch {
                val stepIdx = currentCategoryNumber - 1
                val category = stepArray[stepIdx].stepCategory
                val placeItems = getCategoryPlace(category, 1)

                Log.i("placeItems = ", placeItems.toString())

                placeItems.items.forEach {
                    Log.i("item = ", it.toString())

                    try {
                        val marker = Marker()

                        val longitude = StringBuilder()
                            .append(it.mapx.substring(0, 3))
                            .append(".")
                            .append(it.mapx.substring(3))
                            .toString()
                            .toDouble()

                        val latitude = StringBuilder()
                            .append(it.mapy.substring(0, 2))
                            .append(".")
                            .append(it.mapy.substring(2))
                            .toString()
                            .toDouble()

                        val latLng = LatLng(latitude, longitude)
                        Log.i("latLng = ", latLng.toString())
                        marker.position = latLng
                        marker.map = map
                        marker.onClickListener = Overlay.OnClickListener {
                            true
                        }

                    } catch (e: Exception) {
                        Log.i("error = ", e.toString())
                    }
                }
            }
        }

//        LocationSettingSync { latLng ->
//            Log.i("lating = ", latLng.toString())
//            Log.i("navermap is null = ", (naverMap == null).toString())
//
//            naverMap?.moveCamera(CameraUpdate.scrollTo(latLng))
//        }
    }

    private suspend fun getCategoryPlace(category: PlaceCategory, start: Int): PlaceItemApiOuterResponse {
        for (idx in start..start + 3) {
            val response = ApiAuthClient.categoryPlaceService.getCategoryPlace(
                cityCode = selectedCityCode,
                districtCode = selectedDistrictCode,
                place = category.koreanName,
                start = idx
            )
            if (response.isSuccessful) {
                return response.body() ?: PlaceItemApiOuterResponse()
            }
        }
        return PlaceItemApiOuterResponse()
    }
}