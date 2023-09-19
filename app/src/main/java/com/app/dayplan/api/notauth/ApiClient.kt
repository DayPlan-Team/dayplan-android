package com.app.dayplan.api.notauth

import com.app.dayplan.api.ApiUtil
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiUtil.LOCAL_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiNotAuthService: ApiNotAuthService = retrofit.create(ApiNotAuthService::class.java)
}
