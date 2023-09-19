package com.app.dayplan

import android.app.Application
import com.app.dayplan.util.SharedPreferencesHelper

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        SharedPreferencesHelper.init(this)
    }
}