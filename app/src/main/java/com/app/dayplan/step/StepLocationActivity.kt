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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.app.dayplan.datecourse.Location
import com.app.dayplan.home.HomeBar
import com.app.dayplan.home.TopBar
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.userlocation.LocationSettingSync
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap

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
        intent.getIntExtra("currentCategoryNumber", 1)
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
    fun DateCourseCategoryText(text1: String, text2: String) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text1,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(
                text = text2,
                style = TextStyle(fontWeight = FontWeight.Normal),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
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
            modifier = Modifier
                .height(300.dp)
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