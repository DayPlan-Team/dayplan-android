package com.app.dayplan.course

import com.app.dayplan.step.CourseStage
import com.app.dayplan.step.PlaceCategory

data class CourseView(
    val courseId: Long,
    val placeId: Long,
    val step: Int,
    val placeCategory: PlaceCategory,
    val courseStage: CourseStage,
    val placeName: String,
    val address: String,
    val roadAddress: String,
)

data class CourseViewResponse(
    val groupId: Long,
    val courses: List<CourseView> = emptyList(),
)