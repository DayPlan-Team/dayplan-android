package com.app.dayplan.api.notauth

import com.app.dayplan.api.ApiUtil
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiNotAuthClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiUtil.LOCAL_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitNaverMap = Retrofit.Builder()
        .baseUrl(ApiUtil.NAVER_OPEN_API_REVERSE_COORDINATE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiNotAuthService: ApiNotAuthService = retrofit.create(ApiNotAuthService::class.java)
}
