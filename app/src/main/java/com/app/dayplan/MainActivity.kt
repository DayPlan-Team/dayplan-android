package com.app.dayplan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.dayplan.auth.LoginActivity
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.SharedPreferencesHelper
import com.app.dayplan.verify.VerifyActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val accessToken = SharedPreferencesHelper.accessToken

        Log.i("accessToken = ", accessToken)

        if (accessToken.isNotEmpty()) {
            val intent = Intent(this, VerifyActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        setContent {
            DayplanTheme {

            }
        }
    }
}
