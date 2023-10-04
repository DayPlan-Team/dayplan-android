package com.app.dayplan.api.auth

import com.app.dayplan.api.ApiUtil
import com.app.dayplan.step.CourseGroupResponse
import com.app.dayplan.step.CourseSettingApiRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiCourseService {

    @Headers(ApiUtil.CONTENT_TYPE_APPLICATION_JSON)
    @POST("/content/course")
    suspend fun setCourseAndGetCourseGroupId(
        @Body courseSettingApiRequest: CourseSettingApiRequest,
    ): Response<CourseGroupResponse>

}