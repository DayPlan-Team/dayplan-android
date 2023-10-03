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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dayplan.api.auth.ApiAuthClient
import com.app.dayplan.datecourse.Location
import com.app.dayplan.home.HomeBar
import com.app.dayplan.home.TopBar
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.IntentExtra

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class StepPlaceActivity : ComponentActivity() {

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
            Divider(color = Color.Gray, thickness = 3.dp) // 위의 경계선
//            PlaceItemLocationScreen()
            PlaceItemLocationScreen2()
            HomeBar(this@StepPlaceActivity)
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
    fun PlaceItemLocationScreen() {
        val placeItemsState = remember { mutableStateOf<List<PlaceItemApiResponse>>(emptyList()) }
        val currentContext = LocalContext.current

        LaunchedEffect(key1 = Unit) {
            val stepIdx = currentCategoryNumber - 1
            val category = stepArray[stepIdx].stepCategory
            val placeItems = getCategoryPlace(category, 1)

            placeItemsState.value = placeItems.items
        }

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
                    if(item.telephone.isNotEmpty()) {
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
    fun PlaceItemLocationScreen2() {
        val placeItemsState = remember { mutableStateOf<List<PlaceItemApiResponse>>(emptyList()) }
        val currentContext = LocalContext.current
        val selectedTabIndex = remember { mutableStateOf(0) } // 현재 선택된 탭의 인덱스를 저장하는 state
        val tabTitles = listOf("통합", "추천", "저장")

        // 데이터 로드
        LaunchedEffect(key1 = Unit) {
            val stepIdx = currentCategoryNumber - 1
            val category = stepArray[stepIdx].stepCategory
            val placeItems = getCategoryPlace(category, 1)

            placeItemsState.value = placeItems.items
        }

        // 탭 레이아웃 생성
        Column {
            TabRow(
                selectedTabIndex = selectedTabIndex.value,
                contentColor = MaterialTheme.colorScheme.primary
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

            // 탭에 따라 다른 내용 표시
            when (selectedTabIndex.value) {
                0 -> {
                    // "통합" 탭의 내용
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
    ): PlaceItemApiOuterResponse {
        for (idx in start..start + 3) {
            val response = ApiAuthClient.categoryPlaceService.getCategoryPlace(
                cityCode = selectedCityCode,
                districtCode = selectedDistrictCode,
                place = category,
                start = idx
            )
            if (response.isSuccessful) {
                return response.body() ?: PlaceItemApiOuterResponse()
            }
        }
        return PlaceItemApiOuterResponse()
    }

    private fun applyStepAction(context: Context, placeItemApiResponse: PlaceItemApiResponse) {
        val intent = Intent(context, StepMapActivity::class.java)
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