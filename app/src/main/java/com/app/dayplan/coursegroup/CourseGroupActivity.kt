package com.app.dayplan.coursegroup

import android.content.Context
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dayplan.api.auth.ApiAuthClient
import com.app.dayplan.datecourse.DateCourseLocationCitySettingActivity
import com.app.dayplan.home.HomeBar
import com.app.dayplan.home.TopBar
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.startActivityAndFinish

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class CourseGroupActivity : ComponentActivity() {

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
            DateCoursesNewScreen()
            Divider(color = Color(0xFFEBEBEB), thickness = 4.dp) // 위의 경계선
            DateSavedCoursesScreen()
            HomeBar(this@CourseGroupActivity)
        }
    }

    @Composable
    fun DateCoursesNewScreen() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    this@CourseGroupActivity.startActivityAndFinish(
                        DateCourseLocationCitySettingActivity::class.java
                    )
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
                Text("새로운 코스 만들기")
            }
        }
    }

    @Composable
    fun DateSavedCoursesScreen() {
        val courseGroups = remember {
            mutableStateOf<CourseGroups?>(
                CourseGroups(emptyList())
            )
        }
        val currentContext = LocalContext.current

        // 데이터 로드
        LaunchedEffect(key1 = Unit) {
            val courseGroupsApiResponse = getCourseGroups()
            courseGroups.value = courseGroupsApiResponse?.copy()
        }

        // 탭 레이아웃 생성
        Column {
            Text(text = "내가 만든 코스")
            LazyColumn {
                val courseGroupsItem = courseGroups.value
                if (courseGroupsItem != null) {
                    items(courseGroupsItem.courseGroups) { item ->
                        // 여기에서 각 항목에 대한 UI를 생성합니다.
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { applyStepAction(currentContext, item) }
                        ) {
                            CourseGroupBox(item)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CourseGroupBox(courseGroup: CourseGroup) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = courseGroup.groupName,
                        fontSize = 20.sp
                    )
                    Text(
                        text = courseGroup.cityName,
                        color = Color.Gray
                    )
                    Text(
                        text = courseGroup.districtName,
                        color = Color.Gray
                    )
                }
            }
            Divider(color = Color.Gray, thickness = 1.dp) // 아래의 경계선
        }
    }


    private suspend fun getCourseGroups(): CourseGroups? {
        try {
            val response = ApiAuthClient.courseGroupService.getCourseGroups()

            if (response.isSuccessful && response.body() != null) {
                return response.body()!!
            }
            return null
        } catch (e: Exception) {
            Log.i("error = ", e.toString())
        }
        return null
    }

    private fun applyStepAction(context: Context, response: CourseGroup) {

    }
}