package com.app.dayplan.api.auth

import com.app.dayplan.api.ApiUtil
import com.app.dayplan.step.PlaceCategory
import com.app.dayplan.step.PlaceItemApiOuterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiCategoryPlaceService {
    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @GET("/user/place/search")
    suspend fun getCategoryPlace(
        @Query("citycode") cityCode: Long,
        @Query("districtcode") districtCode: Long,
        @Query("place") place: PlaceCategory,
        @Query("start") start: Int = 1,
    ): Response<PlaceItemApiOuterResponse>
}