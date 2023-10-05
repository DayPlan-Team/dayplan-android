package com.app.dayplan.api.auth

import com.app.dayplan.api.ApiUtil
import com.app.dayplan.course.Course
import com.app.dayplan.course.CourseViewResponse
import com.app.dayplan.step.CourseUpsertApiRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiCourseService {

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @POST("/content/course")
    suspend fun upsertCourse(
        @Body course: Course,
    ): Response<Unit>

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @GET("/content/course")
    suspend fun getCourses(
        @Query("groupId") groupId: Long,
    ): Response<CourseViewResponse>


}