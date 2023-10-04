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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dayplan.datecourse.Location
import com.app.dayplan.home.HomeBar
import com.app.dayplan.home.TopBar
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.IntentExtra

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class StepCategoryActivity : ComponentActivity() {

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
        intent.getIntExtra("currentCategoryNumber", 1)
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
            Divider(color = Color.Gray, thickness = 3.dp)
            DateCourseCategoryThemeBox("카테고리를 선택 해주세요")
            CategorySection()
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
    fun StepSection() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "데이트 코스 선택",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 10.dp),
                fontSize = 25.sp
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                for (idx in 0 until currentCategoryNumber) {
                    if (idx <= stepArray.lastIndex) {
                        val step = stepArray[idx]
                        StepInfo(
                            stepNumber = "step${step.stepNumber}",
                            category = step.stepCategory.koreanName, // Assuming `PlaceCategory` has a `name` property
                            placeName = step.placeName,
                            stage = step.stage,
                        )
                    } else {
                        StepInfo("step${idx + 1}", "", "", StepStage.START)
                    }
                }
            }
        }
    }
    @Composable
    fun CategorySection() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DateCourseCategoryBox(PlaceCategory.CAFE)
            DateCourseCategoryBox(PlaceCategory.MOVIE_THEATER)
            DateCourseCategoryBox(PlaceCategory.RESTAURANT)
            DateCourseCategoryBox(PlaceCategory.ACTIVITY)
        }
    }

    @Composable
    fun DateCourseCategoryBox(
        category: PlaceCategory,
    ) {
        val currentContext = LocalContext.current
        val contextState = rememberUpdatedState(currentContext)

        Button(
            onClick = {
                contextState.value?.let {
                    applyStepAction(it, category)
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

    private fun applyStepAction(context: Context, placeCategory: PlaceCategory) {
        val intent = Intent(context, StepPlaceActivity::class.java)
        intent.putExtra(IntentExtra.CITY_NAME.key, selectedCityName)
        intent.putExtra(IntentExtra.CITY_CODE.key, selectedCityCode)
        intent.putExtra(IntentExtra.DISTRICT_NAME.key, selectedDistrictName)
        intent.putExtra(IntentExtra.DISTRICT_CODE.key, selectedDistrictCode)

        val locationSteps = ArrayList<Steps>()
        for (idx in 0 until currentCategoryNumber) {
            if (idx <= stepArray.lastIndex) {
                locationSteps.add(stepArray[idx])
            }
        }
        locationSteps.add(
            Steps(
                stepNumber = currentCategoryNumber,
                stepCategory = placeCategory,
                stage = StepStage.CATEGORY_FINISH,
            )
        )

        intent.putExtra(IntentExtra.CURRENT_CATEGORY_NUMBER.key, currentCategoryNumber)
        intent.putExtra(IntentExtra.STEPS.key, locationSteps)

        context.startActivity(intent)
        finish()
    }
}