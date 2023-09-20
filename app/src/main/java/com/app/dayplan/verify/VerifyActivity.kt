package com.app.dayplan.verify

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
import com.app.dayplan.reissue.ReissueTokenActivity
import com.app.dayplan.terms.TermsActivity
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.ErrorStatus
import com.app.dayplan.util.ErrorResponse
import com.app.dayplan.util.UserAccountStatus
import com.app.dayplan.util.startActivityAndFinish
import com.google.gson.Gson
import kotlinx.coroutines.launch

class VerifyActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DayplanTheme {
                VerifyScreen()
            }
        }
    }

    @Composable
    fun VerifyScreen() {
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(key1 = Unit) { // key1은 API 호출이 한 번만 이루어지게 하는 역할
            coroutineScope.launch {
                val response = ApiAuthClient.verifyService.verifyUser()

                if (response.isSuccessful) {
                    response.body()?.let {
                        when {
                            it.verified && it.userStatus == UserAccountStatus.NORMAL && it.mandatoryTermsAgreed -> this@VerifyActivity.startActivityAndFinish(HomeActivity::class.java)
                            it.verified && it.userStatus == UserAccountStatus.NORMAL -> this@VerifyActivity.startActivityAndFinish(TermsActivity::class.java)
                            it.verified -> setContent {
                                DayplanTheme {
                                    AccountStatusInvalidScreen()
                                }
                            }
                            else -> this@VerifyActivity.startActivityAndFinish(LoginActivity::class.java)
                        }
                    }
                }

                else {
                    val gson = Gson()
                    val errorString = response.errorBody()?.string()
                    if (errorString != null) {
                        val errorResponse = gson.fromJson(errorString, ErrorResponse::class.java)
                        Log.i("error-response = ", errorResponse.toString())

                        when (ErrorStatus.findErrorStatusByCode(errorResponse.code)) {
                            ErrorStatus.ACCESSTOKEN_EXPIRED -> this@VerifyActivity.startActivityAndFinish(ReissueTokenActivity::class.java)
                            else -> this@VerifyActivity.startActivityAndFinish(LoginActivity::class.java)
                        }
                    }
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