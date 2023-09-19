package com.app.dayplan.api.auth

import com.app.dayplan.api.ApiUtil
import com.app.dayplan.terms.TermsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiTermsService {

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @GET("/user/terms")
    suspend fun getTerms(): Response<TermsResponse>

}