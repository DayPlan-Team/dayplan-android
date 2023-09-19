package com.app.dayplan.api.auth

import com.app.dayplan.api.ApiUtil
import com.app.dayplan.terms.TermsAgreements
import com.app.dayplan.terms.TermsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiTermsService {

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @GET("/user/terms")
    suspend fun getTerms(): Response<TermsResponse>

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @POST("/user/terms")
    suspend fun upsetTermsAgreements(@Body termsAgreements: TermsAgreements): Response<Void>

}