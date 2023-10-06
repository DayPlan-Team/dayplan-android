package com.app.dayplan.home

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.compose.rememberImagePainter
import com.app.dayplan.R
import com.app.dayplan.advertise.ViewPagerAdapter
import com.app.dayplan.coursegroup.CourseGroupActivity
import com.app.dayplan.datecoursesearch.DateCourseLocationCitySearchActivity
import com.app.dayplan.map.MapRegistrationActivity
import com.app.dayplan.step.PlaceCategory
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
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) { // 전체 화면을 차지하는 Box
            TopBar2(modifier = Modifier.align(Alignment.TopCenter))
            // 스크롤 가능한 영역

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 80.dp,
                        bottom = 60.dp
                    )
            ) {
                item { AdvertisementSlider() }
                item { DateCourseSection() }
                item { WhereGoSection() }
                item { Divider(color = Color(0xFFEBEBEB), thickness = 3.dp) }
                item { WhereNotGoSection() }
                item { Divider(color = Color(0xFFEBEBEB), thickness = 3.dp) }
//                item { LocationRegistrationSection() }
                item { RealReviewScreen() }
            }

            // 화면 하단에 위치하는 홈바
            HomeBar2(this@HomeActivity, modifier = Modifier.align(Alignment.BottomCenter))
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
                .height(200.dp)
                .padding(
                    top = 10.dp,
                    bottom = 20.dp
                ),
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
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 20.dp,
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DateCourseRoundedBoxButton(
                text1 = "쉽게 짜는 나만의 코스\n",
                text2 = "데이트 코스짜기"
            ) {
                this@HomeActivity.startActivityAndFinish(
                    CourseGroupActivity::class.java
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            DateCourseRoundedBoxButton(
                text1 = "코스를 짜기 어렵다면\n",
                text2 = "코스 둘러보기",
            ) {
                this@HomeActivity.startActivityAndFinish(
                    DateCourseLocationCitySearchActivity::class.java
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
                .width(150.dp)
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
                        text = "어떤 활동을 하시나요?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp * 1.5f,
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    WhereGoBox(PlaceCategory.CAFE) // CategoryBox에도 동일한 패딩을 적용해야합니다.
                    WhereGoBox(PlaceCategory.ACTIVITY)
                    WhereGoBox(PlaceCategory.PARK)
                    WhereGoBox(PlaceCategory.RESTAURANT)
                    WhereGoBox(PlaceCategory.ZOO)
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    WhereGoBox(PlaceCategory.AQUARIUM) // CategoryBox에도 동일한 패딩을 적용해야합니다.
                    WhereGoBox(PlaceCategory.CONCERT_HALL)
                    WhereGoBox(PlaceCategory.FITNESS)
                    WhereGoBox(PlaceCategory.LIBRARY)
                    WhereGoBox(PlaceCategory.SHOPPING_MALL)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    @Composable
    fun WhereNotGoSection() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween // 요소들을 양쪽 끝으로 분리
                ) {
                    Text(
                        text = "아직 안 가본 곳",
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp * 1.5f,
                    )
                    Text( // 오른쪽에 위치하는 "전체보기>" 텍스트
                        text = "전체보기 >",
                        color = Color.Gray, // 원하는 색상 설정
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Row(
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = "다른 동네들도 채워보세요",
                        color = Color(0xFF7B7B7B),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    WhereNotGoBox("내 주변") // CategoryBox에도 동일한 패딩을 적용해야합니다.
                    WhereNotGoBox("강동구")
                    WhereNotGoBox("강서구")
                    WhereNotGoBox("중랑구")
                    WhereNotGoBox("종로구")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    @Composable
    fun WhereNotGoBox(text: String) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 6.dp, bottom = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) // 이것은 동그란 박스입니다.
            Text(
                text = text,
                fontSize = 10.sp
            ) // 이 텍스트는 박스 위에 나타납니다.
        }
    }

    @Composable
    fun WhereGoBox(placeCategory: PlaceCategory) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 6.dp, bottom = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) // 이것은 동그란 박스입니다.
            Text(
                text = placeCategory.koreanName,
                fontSize = 10.sp
            ) // 이 텍스트는 박스 위에 나타납니다.
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

    @Composable
    fun RealReviewScreen() {
        // 임의의 데이터 (이 부분은 실제 데이터로 변경하셔야 합니다.)
        val reviews = listOf(
            Review(name = "스타벅스", description = "카공 맛집!", ""),
            Review(name = "이디야", description = "커피 맛집!", ""),
            Review(name = "집부실", description = "공부 맛집!", ""),
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                modifier = Modifier.padding(bottom = 5.dp),
                text = "직접 방문한 유저들의 리얼 리뷰",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7B7B7B),
            )

            Spacer(modifier = Modifier.height(16.dp))  // 공간 확보

            LazyRow {
                items(reviews) { review ->
                    ReviewBox(review)
                }
            }
        }
    }

    @Composable
    fun ReviewBox(review: Review) {
        Column(
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .width(200.dp)
                    .shadow(8.dp, RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = rememberImagePainter(data = R.drawable.ad1),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.name,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = review.description,
                fontSize = 10.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    data class Review(
        val name: String,
        val description: String,
        val imageUrl: String,
    )

    @Preview(showBackground = true)
    @Composable
    fun PreviewRealReviewScreen() {
        RealReviewScreen()
    }


    fun Int.dpToPx(dp: Float): Int {
        val density = resources.displayMetrics.density
        return (dp * density).roundToInt()
    }
}