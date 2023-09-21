package com.app.dayplan.util

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity

fun ComponentActivity.startActivityAndFinish(clazz: Class<*>) {
    val intent = Intent(this, clazz)
    startActivity(intent)
    finish()
}

fun ComponentActivity.startActivityAndClearPrevious(clazz: Class<*>) {
    val intent = Intent(this, clazz)
    Log.i("intent ", intent.toString())
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()
}