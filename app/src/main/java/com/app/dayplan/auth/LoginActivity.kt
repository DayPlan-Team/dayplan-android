package com.app.dayplan.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.app.dayplan.R
import com.app.dayplan.api.notauth.ApiClient
import com.app.dayplan.api.SocialType
import com.app.dayplan.home.HomeActivity
import com.app.dayplan.terms.TermsActivity
import com.app.dayplan.ui.theme.DayplanTheme
import com.app.dayplan.util.SharedPreferencesHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : ComponentActivity() {

    private lateinit var  mGoogleSignInClient: GoogleSignInClient

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            DayplanTheme {
                SignInScreen()
            }
        }


    }

    @Preview
    @Composable
    fun SignInScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { signIn() }) {
                Text("구글로 로그인하기")
            }
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account?.idToken
            sendTokenToServer(idToken!!)

        } catch (e: ApiException) {
            Log.w("GoogleSignIn", "Google sign in failed", e)
        }
    }

    private fun sendTokenToServer(idToken: String) {
        val call = ApiClient.apiNotAuthService.sendToken(
            registrationId = SocialType.GOOGLE.registrationId,
            code = idToken
        )
        call.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    Log.i("ServerResponse", "Token sent successfully!")

                    response.body()?.let {

                        Log.i("accessToken", it.accessToken)
                        Log.i("refreshToken", it.refreshToken)

                        SharedPreferencesHelper.accessToken = it.accessToken
                        SharedPreferencesHelper.refreshToken = it.refreshToken

                        // TermsActivity 이동
                        val intent = Intent(this@LoginActivity, TermsActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                } else {
                    Log.w("ServerResponse", "Failed to send token: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Log.e("ServerResponse", "Error: ${t.message}")
            }
        })
    }
}