package com.app.dayplan.home

import android.graphics.Rect
import android.os.Bundle
import android.view.View
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.app.dayplan.R
import com.app.dayplan.advertise.ViewPagerAdapter
import com.app.dayplan.datecourse.DateCourseLocationCitySettingActivity
import com.app.dayplan.map.MapRegistrationActivity
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.startActivityAndFinish
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

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
            TopBar()
            AdvertisementSlider()
            DateCourseSection()
            CategorySection()
            Divider(color = Color.Gray, thickness = 1.dp)
            WhereGoSection()
            LocationRegistrationSection()
            HomeBar(this@HomeActivity)
        }
    }

    // 첫 번째 섹션: 광고를 위한 슬라이드 공간
    @Composable
    fun AdvertisementSlider() {
        val adImages = listOf(R.drawable.ad1, R.drawable.ad2, R.drawable.ad3)
        val adapter = remember { ViewPagerAdapter(adImages) }
        val context = LocalContext.current
        val viewPager = remember { ViewPager2(context) }

        // 자동 슬라이딩을 위한 Coroutine 사용
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(key1 = viewPager) {
            while (true) {
                delay(3000) // 3초마다 페이지 변경
                withContext(Dispatchers.Main) {
                    viewPager.currentItem = (viewPager.currentItem + 1) % adImages.size
                }
            }
        }

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            factory = { context ->
                viewPager.apply {
                    this.adapter = adapter
                }
            },
            update = { view ->
                val recyclerView = view.children.first { it is RecyclerView } as RecyclerView
                recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        val totalWidth = parent.width
                        val maxCardWidth = totalWidth - totalWidth.dpToPx(32f)
                        val cardMargin = (totalWidth - maxCardWidth) / 2
                        if (parent.getChildAdapterPosition(view) == 0) {
                            outRect.right = cardMargin
                            outRect.left = cardMargin // 왼쪽 여백 추가
                        } else if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
                            outRect.left = cardMargin
                            outRect.right = cardMargin // 오른쪽 여백 추가
                        } else {
                            outRect.left = cardMargin
                            outRect.right = cardMargin
                        }
                    }
                })
                // 필요한 경우 view를 업데이트합니다.
            }
        )
    }

    // 두 번째 섹션: 데이트 코스 짜기 및 데이트 코스 둘러보기 버튼
    @Composable
    fun DateCourseSection() {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DateCourseRoundedBoxButton(
                text1 = "쉽게 짜는 나만의 코스\n",
                text2 = "데이트 코스짜기"
            ) {
                this@HomeActivity.startActivityAndFinish(
                    DateCourseLocationCitySettingActivity::class.java
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            DateCourseRoundedBoxButton(
                text1 = "코스를 짜기 어렵다면\n",
                text2 = "코스 둘러보기",
            ) {
                this@HomeActivity.startActivityAndFinish(
                    DateCourseLocationCitySettingActivity::class.java
                )
            }
        }
    }

    @Composable
    fun DateCourseRoundedBoxButton(
        text1: String,
        text2: String,
        onClickAction: () -> Unit,
    ) {
        Button(
            onClick = { onClickAction() },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(70.dp)
                .width(180.dp)
                .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(20.dp)),
            contentPadding = PaddingValues(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF1F1F1),
                contentColor = Color.Black,
            )
        ) {
            DateCourseText(
                text1 = text1,
                text2 = text2,
            )
        }
    }

    @Composable
    fun DateCourseText(text1: String, text2: String) {
        val styledText = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                )
            ) {
                append(text1)
            }
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp * 1.5f
                )
            ) {
                append(text2)
            }
        }

        Text(text = styledText)
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
                    .padding(start = 16.dp, bottom = 16.dp)
            ) {
                CategoryBox("")
                CategoryBox("")
                CategoryBox("")
                CategoryBox("")
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, bottom = 16.dp)
            ) {
                CategoryBox("")
                CategoryBox("")
                CategoryBox("")
                CategoryBox("")
            }
        }
    }

    @Composable
    fun CategoryBox(categoryName: String) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.LightGray)
        ) {
            Text(categoryName, modifier = Modifier.align(Alignment.Center))
        }
    }

    @Composable
    fun WhereGoSection() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = "어디로 가시나요?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp * 1.5f,
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    WhereGoBox("") // CategoryBox에도 동일한 패딩을 적용해야합니다.
                    WhereGoBox("")
                    WhereGoBox("")
                    WhereGoBox("")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    @Composable
    fun WhereGoBox(categoryName: String) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
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
            LocationRegistrationButton {
                this@HomeActivity.startActivityAndFinish(
                    MapRegistrationActivity::class.java
                )
            }
        }
    }

    @Composable
    fun LocationRegistrationButton(onClickAction: () -> Unit) {
        Button(
            onClick = { onClickAction() },
            modifier = Modifier
                .padding(16.dp)
                .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(20.dp)),
            contentPadding = PaddingValues(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF1F1F1),
                contentColor = Color.Black,
            )
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

    fun Int.dpToPx(dp: Float): Int {
        val density = resources.displayMetrics.density
        return (dp * density).roundToInt()
    }
}