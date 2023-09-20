package com.app.dayplan.api.auth

import com.app.dayplan.api.ApiUtil
import com.app.dayplan.verify.VerifyUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiVerifyService {

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @GET("/user/verify")
    suspend fun verifyUser(): Response<VerifyUserResponse>

}