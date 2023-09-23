package com.app.dayplan.datecourse

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.fragment.app.FragmentActivity
import com.app.dayplan.api.auth.ApiAuthClient
import com.app.dayplan.ui.theme.DayplanTheme
import kotlinx.coroutines.launch

class DateCourseLocationDistrictSettingActivity : FragmentActivity() {
    private val selectedCityCode: Long by lazy {
        intent.getLongExtra("cityCode", Location.DEFAULT_CITY_CODE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DayplanTheme {
                DistrictsScreen(selectedCityCode)
            }
        }
    }

    @Composable
    fun DistrictsScreen(cityCode: Long) {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        var cities by remember { mutableStateOf<List<Location>?>(null) }

        LaunchedEffect(key1 = Unit) { // key1은 API 호출이 한 번만 이루어지게 하는 역할
            coroutineScope.launch {
                try {

                    val response = ApiAuthClient.locationService.getDistrictsByCityCode(cityCode)
                    if (response.isSuccessful) {
                        response.body().let {
                            cities = it!!.results
                        }
                    }
                } catch (e: Exception) {
                    Log.e("API Error = ", e.toString())
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                cities == null -> {
                    Log.i("districts List = ", "error")
                    CircularProgressIndicator()
                }

                cities!!.isEmpty() -> {
                    Text(text = "에러가 발생했어요. 잠시 후에 요청 부탁드려요")
                }

                else -> {
                    DistrictCategoryScreen(districts = cities!!)
                }
            }
        }
    }

    @Composable
    fun DistrictCategoryScreen(districts: List<Location> = emptyList()) {
        var selectedDistrictCode by remember { mutableStateOf<Long?>(null) }
        var query by remember { mutableStateOf("") } // 검색 쿼리를 저장하기 위한 상태 변수

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 검색 입력을 위한 기본 TextField 추가
            BasicTextField(
                value = query,
                onValueChange = {
                    query = it
                },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray)
                            .padding(8.dp)
                    ) {
                        if (query.isEmpty()) {
                            Text("도시 검색", color = Color.Gray)
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 필터링된 도시 목록을 표시하기 위한 LazyColumn 추가
            LazyColumn {
                items(districts.filter { it.name.contains(query) }) { district ->
                    val backgroundModifier = if (selectedDistrictCode == district.code) {
                        Modifier.background(Color.Blue)
                    } else {
                        Modifier.background(Color.Transparent)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .hoverIndicator()
                            .clickable {  }
                            .then(backgroundModifier)
                    ) {
                        Text(text = district.name, modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun Modifier.hoverIndicator(): Modifier {
        val interactionSource = remember { MutableInteractionSource() }
        val isHovered by interactionSource.collectIsHoveredAsState()

        val hoverColor = if (isHovered) Color.Blue else Color.Transparent
        return this.indication(interactionSource, rememberRipple())
            .background(hoverColor)
    }
}