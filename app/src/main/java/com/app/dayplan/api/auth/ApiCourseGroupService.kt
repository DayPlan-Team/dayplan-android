package com.app.dayplan.api.auth

import com.app.dayplan.coursegroup.CourseGroups
import com.app.dayplan.api.ApiUtil
import com.app.dayplan.coursegroup.CourseGroupApiRequest
import com.app.dayplan.coursegroup.CourseGroup
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiCourseGroupService {

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @GET("/content/coursegroup")
    suspend fun getCourseGroups(): Response<CourseGroups>

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @POST("/content/coursegroup")
    suspend fun upsertCourseGroup(
        @Body courseGroupApiRequest: CourseGroupApiRequest
    ): Response<CourseGroup>

}