package com.app.dayplan.auth

import com.app.dayplan.util.SharedPreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response

class AuthReissueInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val newRequest = originRequest.newBuilder()
            .header(REFRESH_TOKEN, "$BEARER ${SharedPreferencesHelper.refreshToken}")
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        const val REFRESH_TOKEN = "RefreshToken"
        const val BEARER = "Bearer"
    }
}