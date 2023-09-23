package com.app.dayplan.userlocation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.app.dayplan.auth.LoginActivity
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.SharedPreferencesHelper
import com.app.dayplan.util.startActivityAndFinish
import com.app.dayplan.verify.VerifyActivity

class UserInitialLocationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DayplanTheme {
                GetLocation()
            }
        }

    }

    @Composable
    fun GetLocation() {
        LocationSettingASync()

        val accessToken = SharedPreferencesHelper.accessToken

        Log.i("accessToken = ", accessToken)

        if (accessToken.isNotEmpty()) {
            this.startActivityAndFinish(VerifyActivity::class.java)
        } else {
            this.startActivityAndFinish(LoginActivity::class.java)
        }
    }
}
