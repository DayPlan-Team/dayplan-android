package com.app.dayplan.api.auth

import com.app.dayplan.api.ApiUtil
import com.app.dayplan.reissue.ReissueResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiReissueService {

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @GET("/user/authentication/reissue/accesstoken")
    suspend fun reissueAccessToken(): Response<ReissueResponse>

}