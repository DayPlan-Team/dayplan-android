package com.app.dayplan.datecourse

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.userlocation.LocationViewModel

class DateCourseSettingActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DayplanTheme {
                LocationScreen()
            }
        }
    }

    @Composable
    fun LocationScreen() {

        val viewModel: LocationViewModel = viewModel()
        val city = viewModel.cityCodeState.value
        if (city != null) {
            Log.i("city = ", city.toString())
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                // 서울시 제목
                Spacer(modifier = Modifier.height(8.dp)) // 간격
                // 구 이름
                Column {
                    Text(text = "테스트", style = MaterialTheme.typography.titleMedium)
                }
            }

            // 지도를 표시하는 섹션
            Box(
                modifier = Modifier.weight(8f),
                contentAlignment = Alignment.Center
            ) {
                Log.i("text = ", "test")
            }

//            Box(
//                modifier = Modifier.weight(8f),
//                contentAlignment = Alignment.Center
//            ) {
//                AndroidView(
//                    modifier = Modifier.fillMaxSize(),
//                    factory = { context ->
//                        val view = LayoutInflater.from(context).inflate(R .layout.map_fragment_layout, null)
//                        // 이 부분에서 view 초기화 및 필요한 설정을 할 수 있습니다.
//                        Log.i("지도 로드: ", "지도 띄우기")
//                        view
//                    }
//                )
//            }


        }

    }
}