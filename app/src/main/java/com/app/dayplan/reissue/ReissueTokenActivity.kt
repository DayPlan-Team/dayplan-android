package com.app.dayplan.reissue

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.app.dayplan.api.auth.ApiAuthClient
import com.app.dayplan.auth.LoginActivity
import com.app.dayplan.home.HomeActivity
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.ErrorParser
import com.app.dayplan.util.ErrorResponse
import com.app.dayplan.util.ErrorStatus
import com.app.dayplan.util.SharedPreferencesHelper
import com.app.dayplan.util.startActivityAndFinish
import kotlinx.coroutines.launch

class ReissueTokenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DayplanTheme {
                ReissueScreen()
            }
        }
    }

    @Composable
    fun ReissueScreen() {
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(key1 = Unit) { // key1은 API 호출이 한 번만 이루어지게 하는 역할
            coroutineScope.launch {
                val response = ApiAuthClient.reissueService.reissueAccessToken()

                if (response.isSuccessful) {
                    response.body()?.let {
                        SharedPreferencesHelper.accessToken = it.accessToken
                        this@ReissueTokenActivity.startActivityAndFinish(HomeActivity::class.java)
                    } ?: setContent {
                        DayplanTheme {
                            ServerErrorScreen()
                        }
                    }
                } else {
                    val errorResponse = ErrorParser.parseResponse(
                        response.errorBody(), ErrorResponse.fromErrorStatus(
                            ErrorStatus.REFRESHTOKEN_EXPIRED
                        )
                    )

                    Log.i("로그인 만료 = ", errorResponse.message)

                    this@ReissueTokenActivity.startActivityAndFinish(LoginActivity::class.java)
                }
            }
        }
    }

    @Preview
    @Composable
    fun AccountStatusInvalidScreen() {
        Text(text = "유효하지 않은 계정 상태입니다.")
    }

    @Preview
    @Composable
    fun ServerErrorScreen() {
        Text(text = "죄송합니다. 오류가 발생했습니다.")
    }
}