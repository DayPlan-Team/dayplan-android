package com.app.dayplan.advertise

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.viewpager2.widget.ViewPager2

@Composable
fun ViewPager2Composable(items: List<Int>) {
    val context = LocalContext.current

    AndroidView(
        factory = { context ->
            ViewPager2(context).apply {
                adapter = ViewPagerAdapter(items)
            }
        },
        update = { view ->
            view.adapter = ViewPagerAdapter(items)
        }
    )
}