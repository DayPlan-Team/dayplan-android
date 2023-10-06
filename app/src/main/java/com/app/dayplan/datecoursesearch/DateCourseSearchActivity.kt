package com.app.dayplan.datecoursesearch

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
import com.app.dayplan.datecourse.Location
import com.app.dayplan.home.HomeBar
import com.app.dayplan.home.TopBar
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.IntentExtra

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class DateCourseSearchActivity : ComponentActivity() {

    private val cityCode: Long by lazy {
        intent.getLongExtra(IntentExtra.CITY_CODE.key, Location.DEFAULT_CITY_CODE)
    }

    private val cityName: String by lazy {
        intent.getStringExtra(IntentExtra.CITY_NAME.key) ?: Location.DEFAULT_CITY_NAME
    }

    private val districtCode: Long by lazy {
        intent.getLongExtra(IntentExtra.DISTRICT_CODE.key, Location.DEFAULT_DISTRICT_CODE)
    }

    private val districtName: String by lazy {
        intent.getStringExtra(IntentExtra.DISTRICT_NAME.key) ?: Location.DEFAULT_DISTRICT_NAME
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

        var courseGroupViews by remember { mutableStateOf<CourseGroupSearchResponse?>(null) }

        LaunchedEffect(Unit) {
            courseGroupViews = getCourseGroupSearchResponse()
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar()
            Divider(color = Color(0xFFEBEBEB), thickness = 4.dp) // 위의 경계선

            courseGroupViews?.let {
                CourseGroupViewScreen(courseGroupViews!!)
            }
            HomeBar(this@DateCourseSearchActivity)
        }
    }

    private suspend fun getCourseGroupSearchResponse(): CourseGroupSearchResponse? {
        try {
            val response = ApiAuthClient.courseGroupService.getCourseGroupSearchResponse(
                cityCode = cityCode,
                districtCode = districtCode,
                start = 0,
            )

            if (response.isSuccessful && response.body() != null) {
                return response.body()!!
            }

        } catch (e: Exception) {
            Log.i("error = ", e.toString())
        }

        return null
    }

    private suspend fun getCourseGroupNickName(courseGroupIds: List<Long>): List<CourseGroupWithUserNicknameResponse> {
        try {
            val response = ApiAuthClient.courseGroupService.getCourseGroupWithNickName(
                courseGroupIds = courseGroupIds
            )

            if (response.isSuccessful && response.body() != null) {
                Log.i("response nickname = ", "${response.body()}")
                return response.body()!!
            }

        } catch (e: Exception) {
            Log.i("error = ", e.toString())
        }

        return emptyList()
    }

    @Composable
    fun CourseGroupViewScreen(courseGroupViews: CourseGroupSearchResponse) {
        val courseGroupItems = remember { mutableStateOf<List<CourseGroupItem>>(emptyList()) }
        val courseGroupWithUserNicknameResponse = remember {
            mutableStateOf<Map<Long, CourseGroupWithUserNicknameResponse>>(
                emptyMap()
            )
        }
        val currentContext = LocalContext.current
        val selectedTabIndex = remember { mutableStateOf(0) } // 현재 선택된 탭의 인덱스를 저장하는 state
        val selectedTagIndex = remember { mutableStateOf(0) } // 현재 선택된 태그의 인덱스를 저장하는 state
        val tabTitles = listOf("통합", "추천", "저장")
        val tagTitles = listOf("좋아요순", "최신순", "스크랩순")

        // 데이터 로드
        LaunchedEffect(key1 = courseGroupViews) {
            if (courseGroupViews.courseGroupItems.isNotEmpty()) {
                val groupIds = courseGroupViews.courseGroupItems.map {
                    it.groupId
                }
                courseGroupItems.value = courseGroupViews.courseGroupItems
                courseGroupWithUserNicknameResponse.value = getCourseGroupNickName(groupIds)
                    .associateBy { it.courseGroupId }
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
                        items(courseGroupItems.value) { item ->
                            // 여기에서 각 항목에 대한 UI를 생성합니다.
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { }
                            ) {
                                PlaceBox(item, courseGroupWithUserNicknameResponse.value)
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


    @Composable
    fun PlaceBox(item: CourseGroupItem, nickNameByCourseGroup: Map<Long, CourseGroupWithUserNicknameResponse>) {
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
                        text = "${item.cityName} ${item.districtName}",
                        color = Color.Gray
                    )
                    Text(
                        text = "코스 개수: ${item.courseCategories.size}",
                        color = Color.Gray
                    )
                    nickNameByCourseGroup[item.groupId]?.let {
                        Text(
                            text = "작성자: ${nickNameByCourseGroup[item.groupId]?.userNickName}",
                            color = Color.Gray
                        )
                    }
                }
            }
            Divider(color = Color.Gray, thickness = 1.dp) // 아래의 경계선
        }
    }

//    private fun applyStepAction(context: Context, placeItemApiResponse: PlaceItemApiResponse) {
//        val intent = Intent(context, StepMapActivity::class.java)
//        intent.putExtra(IntentExtra.SELECTED_PLACE_ITEM.key, placeItemApiResponse)
//        intent.putExtra(IntentExtra.COURSE_GROUP.key, courseGroup)
//        intent.putExtra(IntentExtra.CURRENT_CATEGORY_INDEX.key, currentCategoryIndex)
//        context.startActivity(intent)
//        finish()
//    }
}