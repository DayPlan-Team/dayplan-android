package com.app.dayplan.step

data class CourseApiResponse(
    val groupId: Long,
    val courses: List<CourseItem>,
)

data class CourseItem(
    val courseId: Long,
    val placeId: Long,
    val step: Int,
)