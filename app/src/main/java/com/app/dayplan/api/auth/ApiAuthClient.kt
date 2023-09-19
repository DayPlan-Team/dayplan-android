package com.app.dayplan.api.auth

import com.app.dayplan.api.ApiUtil
import com.app.dayplan.api.notauth.ApiNotAuthService
import com.app.dayplan.auth.AuthInterceptor
import com.app.dayplan.util.SharedPreferencesHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiAuthClient {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(SharedPreferencesHelper.accessToken))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiUtil.LOCAL_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val termsService: ApiTermsService = retrofit.create(ApiTermsService::class.java)

}