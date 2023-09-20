package com.app.dayplan.util

import android.content.Intent
import androidx.activity.ComponentActivity

fun ComponentActivity.startActivityAndFinish(clazz: Class<*>) {
    val intent = Intent(this, clazz)
    startActivity(intent)
    finish()
}