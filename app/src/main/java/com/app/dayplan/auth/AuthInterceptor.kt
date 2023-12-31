package com.app.dayplan.auth

import com.app.dayplan.util.SharedPreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val newRequest = originRequest.newBuilder()
            .header(AUTHORIZATION, "$BEARER ${SharedPreferencesHelper.accessToken}")
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer"
    }
}