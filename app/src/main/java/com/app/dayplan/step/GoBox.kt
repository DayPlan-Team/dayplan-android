package com.app.dayplan.step

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun WhereGoBox(text: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentSize()
            .padding(start = 6.dp, end = 6.dp, bottom = 6.dp)
    ) {
        Text(text = text) // 이 텍스트는 박스 위에 나타납니다.
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFF47A14B), CircleShape),

            contentAlignment = Alignment.Center  // 여기에 이 코드를 추가합니다.
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF47A14B)
            )
        }
    }
}

@Composable
fun WhereAlreadySetGoBox(text: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentSize()
            .padding(start = 6.dp, end = 6.dp, bottom = 6.dp)
    ) {
        Text(text = text) // 이 텍스트는 박스 위에 나타납니다.
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFF47A14B), CircleShape),

            contentAlignment = Alignment.Center  // 여기에 이 코드를 추가합니다.
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF47A14B)
            )
        }
    }
}

@Composable
fun StepInfo(stepNumber: String, category: String, placeName: String, stage: CourseStage) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp)) // Optional spacer for separation

        when(stage) {
            CourseStage.START -> WhereGoBox(stepNumber, Icons.Default.Create)
            CourseStage.CATEGORY_FINISH -> WhereAlreadySetGoBox(stepNumber, Icons.Default.Create)
            CourseStage.PLACE_FINISH -> WhereAlreadySetGoBox(stepNumber, Icons.Default.Done)
        }

        Text(text = category)
        Text(text = placeName)
    }
}