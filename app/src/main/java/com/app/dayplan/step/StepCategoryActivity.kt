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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dayplan.api.auth.ApiAuthClient
import com.app.dayplan.course.Course
import com.app.dayplan.course.CourseView
import com.app.dayplan.course.CourseViewResponse
import com.app.dayplan.coursegroup.CourseGroup
import com.app.dayplan.home.HomeBar
import com.app.dayplan.home.TopBar
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.IntentExtra
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class StepCategoryActivity : ComponentActivity() {

    private val courseGroup: CourseGroup by lazy {
        intent.getSerializableExtra(IntentExtra.COURSE_GROUP.key, CourseGroup::class.java)!!
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

        var courseViewResponse by remember { mutableStateOf<CourseViewResponse?>(null) }

        LaunchedEffect(Unit) {
            courseViewResponse = getCourses()
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar()
            courseViewResponse?.let {
                StepSection(it)
                Divider(color = Color.Gray, thickness = 3.dp)
                DateCourseCategoryThemeBox("카테고리를 선택 해주세요")
                CategorySection(it)
            }
            HomeBar(this@StepCategoryActivity)
        }
    }

    @Composable
    fun DateCourseCategoryThemeBox(
        text1: String,
    ) {
        Text(
            text = text1,
            fontSize = 20.sp,
        )
    }

    @Composable
    fun StepSection(courseViewsResponse: CourseViewResponse) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically, // 이 줄을 추가했습니다.
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "데이트 코스 선택",
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 10.dp),
                    fontSize = 25.sp
                )

                val courses = courseViewsResponse.courses
                Log.i("courses Button = ", "${courses}")
                if (courses.isNotEmpty() && courses.all { it.courseStage == CourseStage.PLACE_FINISH }) {
                    Button(
                        onClick = { /*TODO*/ },
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

            LazyRow(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {

                val courses = courseViewsResponse.courses
                items(courses.size) { index ->
                    StepInfo(
                        stepNumber = "step${courses[index].step}",
                        category = courses[index].placeCategory.koreanName,
                        placeName = courses[index].placeName,
                        stage = courses[index].courseStage,
                    )
                }

                if (courses.isEmpty() || courses.all { it.courseStage == CourseStage.PLACE_FINISH }) {
                    item {
                        StepInfo(
                            stepNumber = "step${courses.size + 1}",
                            category = "",
                            placeName = "",
                            stage = CourseStage.START,
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun CategorySection(courseViews: CourseViewResponse) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DateCourseCategoryBox(PlaceCategory.CAFE, courseViews)
            DateCourseCategoryBox(PlaceCategory.MOVIE_THEATER, courseViews)
            DateCourseCategoryBox(PlaceCategory.RESTAURANT, courseViews)
            DateCourseCategoryBox(PlaceCategory.ACTIVITY, courseViews)
        }
    }

    @Composable
    fun DateCourseCategoryBox(
        category: PlaceCategory,
        courseViews: CourseViewResponse,
    ) {
        val currentContext = LocalContext.current
        val contextState = rememberUpdatedState(currentContext)
        val coroutineScope = rememberCoroutineScope()

        Button(
            onClick = {
                contextState.value?.let {
                    coroutineScope.launch {
                        applyStepAction(it, category, courseViews)
                    }
                }
            },
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp)
                .height(40.dp)
                .width(300.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF1F1F1),
                contentColor = Color.Black,
            ),
        ) {
            DateCourseCategoryText(category.koreanName, category.comment)
        }
    }

    @Composable
    fun DateCourseCategoryText(text1: String, text2: String) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text1,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(
                text = text2,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }

    @Composable
    fun WhereGoPlusBox() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentSize()
        ) {
            Text(text = "step add") // 이 텍스트는 박스 위에 나타납니다.
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


    private suspend fun getCourses(): CourseViewResponse{
        try {
            val response = ApiAuthClient.courseService.getCourses(courseGroup.groupId)

            if (response.isSuccessful && response.body() != null) {
                Log.i("response.size = ", response.body()!!.courses.toString())
                return response.body()!!
            }

        } catch (e: Exception) {
            Log.i("e =", e.toString())
        }

        return CourseViewResponse(
            groupId = courseGroup.groupId,
            emptyList()
        )
    }


    private suspend fun applyStepAction(
        context: Context,
        placeCategory: PlaceCategory,
        courseViews: CourseViewResponse,
    ) {
        try {
            val response = ApiAuthClient.courseService.upsertCourse(
                Course(
                    courseId = 0L,
                    groupId = courseGroup.groupId,
                    step = courseViews.courses.size + 1,
                    placeCategory = placeCategory,
                    placeId = 0L,
                )
            )

            if (response.isSuccessful && response.body() != null) {
                Log.i("response = ", "response success!!!")
                val intent = Intent(context, StepPlaceActivity::class.java)
                intent.putExtra(IntentExtra.COURSE_GROUP.key, courseGroup)
                intent.putExtra(IntentExtra.CURRENT_CATEGORY_INDEX.key, courseViews.courses.size)
                context.startActivity(intent)
                finish()
            }
        } catch (e: Exception) {
            Log.e("category step Error = ", e.toString())
        }
    }
}