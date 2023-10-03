package com.app.dayplan.home

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.dayplan.R
import com.app.dayplan.util.startActivityAndFinish

@Composable
fun HomeBar(activity: ComponentActivity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChatIcon(
            modifier = Modifier
                .size(40.dp)
        )
        HomeIcon(
            activity = activity,
            modifier = Modifier
                .size(30.dp)
        )
        FunctionIcon(
            modifier = Modifier
                .size(30.dp)
        )
        PersonIcon(
            modifier = Modifier
                .size(30.dp)
        )
    }
}

@Composable
fun HomeBar2(activity: ComponentActivity, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }
    var iconSize = 30.dp
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)  // 원하는 높이를 지정하세요
            .padding(
                top = 20.dp,
                bottom = 20.dp,
                start = 30.dp,
                end = 30.dp,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HomeIcon(
            activity = activity,
            modifier = Modifier
                .size(iconSize)
        )
        ChatIcon(
            modifier = Modifier
                .size(iconSize)
        )
        FunctionIcon(
            modifier = Modifier
                .size(iconSize)
                .clickable {
                    showDialog = !showDialog
                }
        )
        PersonIcon(
            modifier = Modifier
                .size(iconSize)
        )
    }
    if (showDialog) {
        MyDialog(showDialog) {showDialog = false}
    }

}

@Composable
fun MyDialog(showDialog: Boolean, onDismissRequest: () -> Unit) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false)) {
            // 배경색과 투명도 설정
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp)
                    .background(Color(0xFF47A14B).copy(alpha = 0.8f))
                    .clickable(onClick = onDismissRequest) // 여기에 추가
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 80.dp)
                ) {
                    Text(
                        text = "수동으로 찍기",
                        modifier = Modifier
                            .clickable { /* 클릭 이벤트 처리 */ }
                    )
                    Text(
                        text = "리뷰 작성하기",
                        modifier = Modifier
                            .clickable { /* 클릭 이벤트 처리 */ }
                    )
                    Text(
                        text = "빠른 동작 추가하기",
                        modifier = Modifier
                            .clickable { /* 클릭 이벤트 처리 */ }
                    )
                }
            }
        }
    }
}


@Composable
fun HomeIcon(activity: ComponentActivity, modifier: Modifier = Modifier) {
    val icon: Painter = painterResource(id = R.drawable.home_outline)
    Icon(
        painter = icon,
        contentDescription = null,
        modifier = modifier
            .size(60.dp)
            .clickable {
            activity.startActivityAndFinish(HomeActivity::class.java)
        }
    )
}

@Composable
fun ChatIcon(modifier: Modifier = Modifier) {
    val icon: Painter = painterResource(id = R.drawable.chat_processing_outline)
    Icon(
        painter = icon,
        contentDescription = null,
        modifier = modifier,
    )
}

@Composable
fun PersonIcon(modifier: Modifier = Modifier) {
    val icon: Painter = painterResource(id = R.drawable.account_outline)
    Icon(
        painter = icon,
        contentDescription = null,
        modifier = modifier,
    )
}

@Composable
fun FunctionIcon(modifier: Modifier = Modifier) {
    val icon: Painter = painterResource(id = R.drawable.plus_circle_outline)
    Icon(
        painter = icon,
        contentDescription = null,
        modifier = modifier,
    )
}


@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.dayplan),
            contentDescription = null,
            modifier = Modifier
                .size(34.dp, 26.dp)
        )
        SearchBar(
            modifier = Modifier
                .fillMaxHeight()
                .weight(8f)
        )
        BookMarkIcon(
            modifier = Modifier
                .size(26.dp)
                .weight(1f)
        )
        NotificationIcon(
            modifier = Modifier
                .size(26.dp)
                .weight(1f)
        )
    }
}

@Composable
fun TopBar2(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.dayplan),
            contentDescription = null,
            modifier = Modifier
                .size(34.dp, 26.dp)
        )
        SearchBar(
            modifier = Modifier
                .fillMaxHeight()
                .weight(8f)
        )
        BookMarkIcon(
            modifier = Modifier
                .size(26.dp)
                .weight(1f)
        )
        NotificationIcon(
            modifier = Modifier
                .size(26.dp)
                .weight(1f)
        )
    }
}


@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .height(48.dp)
            .background(Color(0xFFF0F0F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp)
    ) {
        BasicTextField(
            value = "",
            onValueChange = {},
            textStyle = TextStyle.Default.copy(color = Color.Black),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun BookMarkIcon(modifier: Modifier = Modifier) {
    val icon: Painter = painterResource(id = R.drawable.bookmark_outline)
    Icon(
        painter = icon,
        contentDescription = null,
        tint = Color(0xFFCCCCCC)
    )
}

@Composable
fun NotificationIcon(modifier: Modifier = Modifier) {
    val icon: Painter = painterResource(id = R.drawable.bell_outline)
    Icon(
        painter = icon,
        contentDescription = null,
        tint = Color(0xFFCCCCCC)
    )
}