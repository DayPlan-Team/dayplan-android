package com.app.dayplan.api.auth

import com.app.dayplan.api.ApiUtil
import com.app.dayplan.datecourse.Location
import com.app.dayplan.datecourse.LocationOuterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiLocationService {

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @GET("/user/location/city")
    suspend fun getCities(): Response<LocationOuterResponse<List<Location>>>



    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @GET("/user/location/city/{cityCode}/districts")
    suspend fun getDistrictsByCityCode(
        @Path("cityCode") cityCode: Long,
    ): Response<LocationOuterResponse<List<Location>>>
}