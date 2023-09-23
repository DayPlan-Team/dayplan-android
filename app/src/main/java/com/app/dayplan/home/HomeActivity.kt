package com.app.dayplan.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.dayplan.datecourse.DateCourseLocationCitySettingActivity
import com.app.dayplan.map.MapRegistrationActivity
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.startActivityAndFinish

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DayplanTheme {
                HomeScreen()
            }
        }
    }

    @Composable
    fun HomeScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AdvertisementSlider()
            DateCourseSection()
            CategorySection()
            LocationRegistrationSection()
        }
    }

    // 첫 번째 섹션: 광고를 위한 슬라이드 공간
    @Composable
    fun AdvertisementSlider() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Color.Gray)
        ) {
            Text("광고 슬라이드", modifier = Modifier.align(Alignment.Center))
        }
    }

    // 두 번째 섹션: 데이트 코스 짜기 및 데이트 코스 둘러보기 버튼
    @Composable
    fun DateCourseSection() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RoundedBoxButton(text = "데이트 코스 짜기") { this@HomeActivity.startActivityAndFinish(DateCourseLocationCitySettingActivity::class.java) }
            RoundedBoxButton(text = "데이트 코스 둘러보기") { this@HomeActivity.startActivityAndFinish(DateCourseLocationCitySettingActivity::class.java) }
        }
    }

    @Composable
    fun RoundedBoxButton(text: String, onClickAction: () -> Unit) {
        Button(
            onClick = { onClickAction() },
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Blue, shape = RoundedCornerShape(20.dp)),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(text)
        }
    }

    // 세 번째 섹션: 카테고리 박스 4개
    @Composable
    fun CategorySection() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                CategoryBox("카테고리 1")
                CategoryBox("카테고리 2")
                CategoryBox("카테고리 3")
                CategoryBox("카테고리 4")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    @Composable
    fun CategoryBox(categoryName: String) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.LightGray)
        ) {
            Text(categoryName, modifier = Modifier.align(Alignment.Center))
        }
    }

    @Composable
    fun LocationRegistrationSection() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LocationRegistrationButton { this@HomeActivity.startActivityAndFinish(MapRegistrationActivity::class.java) }
        }
    }

    @Composable
    fun LocationRegistrationButton(onClickAction: () -> Unit) {
        Button(
            onClick = { onClickAction() },
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Blue, shape = RoundedCornerShape(20.dp)),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text("위치 등록 하기")
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun PreviewHomeScreen() {
        DayplanTheme {
            HomeScreen()
        }
    }
}