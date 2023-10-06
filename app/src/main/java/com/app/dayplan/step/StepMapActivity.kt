package com.app.dayplan.step

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.app.dayplan.api.auth.ApiAuthClient
import com.app.dayplan.course.Course
import com.app.dayplan.course.CourseView
import com.app.dayplan.coursegroup.CourseGroup
import com.app.dayplan.home.HomeActivity
import com.app.dayplan.home.HomeBar
import com.app.dayplan.home.TopBar
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.IntentExtra
import com.app.dayplan.util.intentSerializable
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class StepMapActivity : ComponentActivity() {

    private val courseGroup: CourseGroup by lazy {
        intent.intentSerializable(IntentExtra.COURSE_GROUP.key, CourseGroup::class.java)
            ?: CourseGroup(
                groupId = 0L,
                groupName = "",
                cityCode = 0L,
                cityName = "",
                districtCode = 0L,
                districtName = "",
            )
    }
    private val currentCategoryIndex: Int by lazy {
        intent.getIntExtra(IntentExtra.CURRENT_CATEGORY_INDEX.key, 0)
    }

    private val selectedPlaceItem: PlaceItemApiResponse by lazy {
        intent.intentSerializable(IntentExtra.SELECTED_PLACE_ITEM.key, PlaceItemApiResponse::class.java)
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

        var courseViews by remember { mutableStateOf<List<CourseView>>(emptyList()) }

        LaunchedEffect(Unit) {
            courseViews = getCourses()
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar()
            StepSection()
            PlaceBox(courseViews)
            HomeBar(this@StepMapActivity)
        }
    }

    private suspend fun getCourses(): List<CourseView> {
        try {
            val response = ApiAuthClient.courseService.getCourses(courseGroup.groupId)

            if (response.isSuccessful && response.body() != null) {
                return response.body()!!.courses
            }

        } catch (e: Exception) {
        }

        return emptyList()
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

            val latLng = LatLng(selectedPlaceItem.latitude, selectedPlaceItem.longitude)
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
    fun PlaceBox(courseView: List<CourseView>) {
        val coroutineScope = rememberCoroutineScope()
        val currentContext = LocalContext.current

        var isButtonEnabled = courseView.isNotEmpty()

        LaunchedEffect(courseView) {
            if (courseView.isNotEmpty()) {
                isButtonEnabled = true
            }
        }

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
                    onClick = {
                        if (isButtonEnabled) {
                            coroutineScope.launch { setCourse(currentContext, courseView) }
                        }
                    },
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

    private suspend fun setCourse(context: Context, courseViews: List<CourseView>) {
        Log.i("currentCategoryIndex = ", "$currentCategoryIndex")
        Log.i("courseViews = ", "$courseViews")
        Log.i("courseID = ", "${courseViews[currentCategoryIndex].courseId}")

        val course = Course(
            groupId = courseGroup.groupId,
            step = currentCategoryIndex + 1,
            placeId = selectedPlaceItem.placeId,
            placeCategory = courseViews[currentCategoryIndex].placeCategory,
            courseId = courseViews[currentCategoryIndex].courseId,
        )

        try {
            val response = ApiAuthClient.courseService.upsertCourse(course)

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.i("responseBody = ", responseBody.toString())

                if (responseBody != null) {

                    Log.i("responseBody = ", responseBody.toString())

                    val intent = Intent(context, StepCategoryActivity::class.java)
                    intent.putExtra(IntentExtra.COURSE_GROUP.key, courseGroup)
                    intent.putExtra(IntentExtra.CURRENT_CATEGORY_INDEX.key, currentCategoryIndex)
                    context.startActivity(intent)
                    finish()

                } else throw Exception()
            } else throw Exception()
        } catch (e: Exception) {
            Log.i("error  = ", e.toString())
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
            finish()
        }
    }
}