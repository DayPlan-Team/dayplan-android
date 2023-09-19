package com.app.dayplan.api.notauth

import com.app.dayplan.api.ApiUtil
import com.app.dayplan.auth.TokenResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiNotAuthService {

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @GET("/user/registration/social/{registrationId}")
    fun sendToken(
        @Path("registrationId") registrationId: String,
        @Query("code") code: String,
    ): Call<TokenResponse>

}
