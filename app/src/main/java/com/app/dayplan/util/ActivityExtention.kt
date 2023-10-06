package com.app.dayplan.util

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import java.io.Serializable

fun ComponentActivity.startActivityAndFinish(clazz: Class<*>) {
    val intent = Intent(this, clazz)
    startActivity(intent)
    finish()
}

fun ComponentActivity.startActivityAndFinishNext(clazz: Class<*>, nextClazz: Class<*>) {
    val intent = Intent(this, clazz)
    intent.putExtra("NEXT_ACTIVITY", nextClazz.canonicalName)
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

fun <T: Serializable> Intent.intentSerializable(key: String, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getSerializableExtra(key, clazz)
    } else {
        this.getSerializableExtra(key) as T?
    }
}