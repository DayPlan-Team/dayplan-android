package com.app.dayplan.api.auth

import com.app.dayplan.api.ApiUtil
import com.app.dayplan.auth.AuthInterceptor
import com.app.dayplan.auth.AuthReissueInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiAuthClient {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    private val okHttpClientReissue = OkHttpClient.Builder()
        .addInterceptor(AuthReissueInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiUtil.LOCAL_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitReissue = Retrofit.Builder()
        .baseUrl(ApiUtil.LOCAL_BASE_URL)
        .client(okHttpClientReissue)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val termsService: ApiTermsService = retrofit.create(ApiTermsService::class.java)
    val verifyService: ApiVerifyService = retrofit.create(ApiVerifyService::class.java)
    val locationGeocodeService: ApiLocationGeocodeService = retrofit.create(ApiLocationGeocodeService::class.java)


    val reissueService: ApiReissueService = retrofitReissue.create(ApiReissueService::class.java)
}