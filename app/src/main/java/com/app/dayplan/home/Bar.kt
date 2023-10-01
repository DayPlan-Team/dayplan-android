package com.app.dayplan.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.app.dayplan.R
import com.app.dayplan.util.startActivityAndFinish
import androidx.activity.ComponentActivity

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
                .size(26.dp)
        )
        HomeIcon(
            activity = activity,
            modifier = Modifier
                .size(26.dp)
        )
        PersonIcon(
            modifier = Modifier
                .size(26.dp)
        )
    }
}

@Composable
fun HomeIcon(activity: ComponentActivity, modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.Home,
        contentDescription = null,
        tint = Color(0xFFCCCCCC),
        modifier = modifier.clickable {
            activity.startActivityAndFinish(HomeActivity::class.java)
        }
    )
}

@Composable
fun ChatIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.MailOutline,
        contentDescription = null,
        tint = Color(0xFFCCCCCC)
    )
}

@Composable
fun PersonIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.Person,
        contentDescription = null,
        tint = Color(0xFFCCCCCC)
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
        FavoriteIcon(
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
fun FavoriteIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.Star,
        contentDescription = null,
        tint = Color(0xFFCCCCCC)
    )
}

@Composable
fun NotificationIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.Notifications,
        contentDescription = null,
        tint = Color(0xFFCCCCCC)
    )
}