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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dayplan.api.auth.ApiAuthClient
import com.app.dayplan.course.CourseView
import com.app.dayplan.coursegroup.CourseGroup
import com.app.dayplan.home.HomeBar
import com.app.dayplan.home.TopBar
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.IntentExtra
import com.app.dayplan.util.intentSerializable

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class StepPlaceActivity : ComponentActivity() {

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
            StepSection(courseViews)
            Divider(color = Color(0xFFEBEBEB), thickness = 4.dp) // 위의 경계선
            PlaceItemLocationScreen(courseViews)
            HomeBar(this@StepPlaceActivity)
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
    fun StepSection(courseViews: List<CourseView>) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "데이트 코스 선택",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 10.dp),
                    fontSize = 25.sp
                )
            }

            LazyRow(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                items(courseViews.size) { course ->
                    StepInfo(
                        stepNumber = "step${courseViews[course].step}",
                        category = courseViews[course].placeCategory.koreanName,
                        placeName = courseViews[course].placeName,
                        stage = courseViews[course].courseStage,
                    )
                }
            }
        }
    }

    @Composable
    fun PlaceBox(item: PlaceItemApiResponse) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = item.title,
                        fontSize = 20.sp
                    )
                    Text(
                        text = item.roadAddress,
                        color = Color.Gray
                    )
                    if (item.telephone.isNotEmpty()) {
                        Text(
                            text = item.telephone,
                            color = Color.Blue
                        )
                    }
                }
            }
            Divider(color = Color.Gray, thickness = 1.dp) // 아래의 경계선
        }
    }

    @Composable
    fun PlaceItemLocationScreen(courseViews: List<CourseView>) {
        val placeItemsState = remember { mutableStateOf<List<PlaceItemApiResponse>>(emptyList()) }
        val currentContext = LocalContext.current
        val selectedTabIndex = remember { mutableStateOf(0) } // 현재 선택된 탭의 인덱스를 저장하는 state
        val selectedTagIndex = remember { mutableStateOf(0) } // 현재 선택된 태그의 인덱스를 저장하는 state
        val tabTitles = listOf("통합", "추천", "저장")
        val tagTitles = listOf("좋아요순", "최신순", "스크랩순")

        // 데이터 로드
        LaunchedEffect(key1 = courseViews) {
            if (courseViews.isNotEmpty()) {
                val category = courseViews[currentCategoryIndex].placeCategory
                val placeItems = getCategoryPlace(category, 1)

                placeItemsState.value = placeItems.items
            }
        }

        val customGreen = Color(0xFF47A14B)

        // 탭 레이아웃 생성
        Column {
            TabRow(
                selectedTabIndex = selectedTabIndex.value,
                contentColor = Color.Black,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex.value]),
                        color = customGreen,
                        height = 4.dp // 이 값은 선택된 탭 아래의 지시자 높이입니다. 원하는대로 조절하실 수 있습니다.
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = selectedTabIndex.value == index,
                        onClick = {
                            selectedTabIndex.value = index
                        }
                    )
                }
            }

            // 태그 레이아웃
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                tagTitles.forEachIndexed { index, title ->
                    Text(
                        text = title,
                        modifier = Modifier
                            .clickable {
                                selectedTagIndex.value = index
                            }
                            .background(
                                if (selectedTagIndex.value == index) customGreen.copy(
                                    alpha = 0.1f
                                ) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(8.dp),
                        color = if (selectedTagIndex.value == index) customGreen else Color.Gray
                    )
                }
            }

            // 탭 및 태그에 따라 다른 내용 표시
            when (selectedTabIndex.value) {
                0 -> {
                    // "통합" 탭의 내용
                    // 태그에 따라 데이터를 필터링하거나 정렬하는 코드를 추가할 수 있습니다.
                    LazyColumn {
                        items(placeItemsState.value) { item ->
                            // 여기에서 각 항목에 대한 UI를 생성합니다.
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { applyStepAction(currentContext, item) }
                            ) {
                                PlaceBox(item)
                            }
                        }
                    }
                }

                1 -> {
                    // "추천" 탭의 내용
                    // 추천 관련 내용을 여기에 추가하세요.
                }

                2 -> {
                    // "저장" 탭의 내용
                    // 저장 관련 내용을 여기에 추가하세요.
                }
            }
        }
    }


    private suspend fun getCategoryPlace(
        category: PlaceCategory,
        start: Int
    ): PlaceSearchItemApiResponse {
        for (idx in start..start + 3) {
            val response = ApiAuthClient.categoryPlaceService.getCategoryPlace(
                cityCode = courseGroup.cityCode,
                districtCode = courseGroup.districtCode,
                place = category,
                start = idx
            )
            if (response.isSuccessful) {
                return response.body() ?: PlaceSearchItemApiResponse()
            }
        }
        return PlaceSearchItemApiResponse()
    }

    private fun applyStepAction(context: Context, placeItemApiResponse: PlaceItemApiResponse) {
        val intent = Intent(context, StepMapActivity::class.java)
        intent.putExtra(IntentExtra.SELECTED_PLACE_ITEM.key, placeItemApiResponse)
//        intent.putExtra(IntentExtra.COURSE_GROUP_ID.key, courseGroup.groupId)
//        intent.putExtra(IntentExtra.COURSE_GROUP_NAME.key, courseGroup.groupName)
//        intent.putExtra(IntentExtra.CITY_CODE.key, courseGroup.cityCode)
//        intent.putExtra(IntentExtra.CITY_NAME.key, courseGroup.cityName)
//        intent.putExtra(IntentExtra.DISTRICT_CODE.key, courseGroup.districtCode)
//        intent.putExtra(IntentExtra.DISTRICT_NAME.key, courseGroup.districtName)
        intent.putExtra(IntentExtra.COURSE_GROUP.key, courseGroup)
        intent.putExtra(IntentExtra.CURRENT_CATEGORY_INDEX.key, currentCategoryIndex)
        context.startActivity(intent)
        finish()
    }
}