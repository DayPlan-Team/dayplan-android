package com.app.dayplan.api.auth

import com.app.dayplan.api.ApiUtil
import com.app.dayplan.userlocation.Coordinates
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiLocationGeocodeService {

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @POST("/user/location")
    suspend fun updateUserLocation(
        @Body coordinates: Coordinates,
    ): Response<Void>

}