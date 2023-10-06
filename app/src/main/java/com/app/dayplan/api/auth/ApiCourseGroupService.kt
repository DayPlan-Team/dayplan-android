package com.app.dayplan.api.auth

import com.app.dayplan.coursegroup.CourseGroups
import com.app.dayplan.api.ApiUtil
import com.app.dayplan.coursegroup.CourseGroupApiRequest
import com.app.dayplan.coursegroup.CourseGroup
import com.app.dayplan.datecoursesearch.CourseGroupSearchResponse
import com.app.dayplan.datecoursesearch.CourseGroupWithUserNicknameResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiCourseGroupService {

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @GET("/content/coursegroup")
    suspend fun getCourseGroup(): Response<CourseGroups>

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @POST("/content/coursegroup")
    suspend fun upsertCourseGroup(
        @Body courseGroupApiRequest: CourseGroupApiRequest
    ): Response<CourseGroup>

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @GET("/content/coursegroup/search/district")
    suspend fun getCourseGroupSearchResponse(
        @Query("cityCode") cityCode: Long,
        @Query("districtCode") districtCode: Long,
        @Query("start") start: Int,
    ): Response<CourseGroupSearchResponse>

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @GET("/content/coursegroup/search/nickname")
    suspend fun getCourseGroupWithNickName(
        @Query("courseGroupIds") courseGroupIds: List<Long>,
    ): Response<List<CourseGroupWithUserNicknameResponse>>

}