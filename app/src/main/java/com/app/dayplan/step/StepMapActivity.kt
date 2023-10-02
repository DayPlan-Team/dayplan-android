package com.app.dayplan.step

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.app.dayplan.datecourse.Location
import com.app.dayplan.home.HomeBar
import com.app.dayplan.home.TopBar
import com.app.dayplan.map.MapRegistrationActivity
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.IntentExtra
import com.app.dayplan.util.startActivityAndFinish
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class StepMapActivity : ComponentActivity() {

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
        intent.getSerializableExtra("steps", ArrayList::class.java) as? ArrayList<Steps>
            ?: arrayListOf()
    }

    private val selectedPlaceItem: PlaceItemApiResponse by lazy {
        intent.getSerializableExtra("selectedPlaceItem", PlaceItemApiResponse::class.java)
            ?: PlaceItemApiResponse()
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
            PlaceBox()
            HomeBar(this@StepMapActivity)
        }
    }


    @Composable
    fun StepSection() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            UserStayedLocationScreen()
        }
    }

    @Composable
    fun UserStayedLocationScreen() {
        val naverMapState = remember { mutableStateOf<NaverMap?>(null) }

        NaverMapView { map ->
            naverMapState.value = map

            val latitude = StringBuilder()
                .append(selectedPlaceItem.mapy.substring(0, 2))
                .append(".")
                .append(selectedPlaceItem.mapy.substring(2))
                .toString()
                .toDouble()

            val longitude = StringBuilder()
                .append(selectedPlaceItem.mapx.substring(0, 3))
                .append(".")
                .append(selectedPlaceItem.mapx.substring(3))
                .toString()
                .toDouble()

            val latLng = LatLng(latitude, longitude)
            map.moveCamera(CameraUpdate.scrollTo(latLng))
            map.moveCamera(CameraUpdate.zoomTo(16.0))

            val marker = Marker()

            marker.position = latLng
            marker.map = map
            marker.onClickListener = Overlay.OnClickListener {
                true
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
            modifier = Modifier
                .height(400.dp)
        )
    }

    @Composable
    fun PlaceBox() {
        Column {
            Divider(color = Color.Gray, thickness = 1.dp) // 아래의 경계선
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = selectedPlaceItem.title,
                        fontSize = 20.sp
                    )
                    Text(
                        text = selectedPlaceItem.roadAddress,
                        color = Color.Gray
                    )
                    if (selectedPlaceItem.telephone.isNotEmpty()) {
                        Text(
                            text = selectedPlaceItem.telephone,
                            color = Color.Blue
                        )
                    }
                    if (selectedPlaceItem.description.isNotEmpty()) {
                        Text(
                            text = selectedPlaceItem.description,
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(20.dp)),
                    contentPadding = PaddingValues(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF1F1F1),
                        contentColor = Color.Black,
                    )
                ) {
                    Text("선택")
                }
            }
        }
    }

    private fun applyStepAction(context: Context, placeItemApiResponse: PlaceItemApiResponse) {
        val intent = Intent(context, StepMapActivity::class.java)

        val nextCurrentCategoryNumber = currentCategoryNumber + 1
        val nextSteps = arrayListOf<Steps>()

        stepArray.forEach {
            nextSteps.add(it)
        }

        TODO()


        intent.putExtra(IntentExtra.CITY_NAME.key, selectedCityName)
        intent.putExtra(IntentExtra.CITY_CODE.key, selectedCityCode)
        intent.putExtra(IntentExtra.DISTRICT_NAME.key, selectedDistrictName)
        intent.putExtra(IntentExtra.DISTRICT_CODE.key, selectedDistrictCode)
        intent.putExtra(IntentExtra.CURRENT_CATEGORY_NUMBER.key, currentCategoryNumber)
        intent.putExtra(IntentExtra.STEPS.key, stepArray)
        intent.putExtra(IntentExtra.SELECTED_PLACE_ITEM.key, placeItemApiResponse)
        context.startActivity(intent)
        finish()
    }

}