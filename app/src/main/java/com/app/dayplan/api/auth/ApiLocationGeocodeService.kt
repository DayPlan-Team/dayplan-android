package com.app.dayplan.api.auth

import com.app.dayplan.api.ApiUtil
import com.app.dayplan.userlocation.AddressCodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiLocationGeocodeService {

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @GET("/user/location/geocode")
    suspend fun getAddressCodeByGeocode(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): Response<AddressCodeResponse>

}