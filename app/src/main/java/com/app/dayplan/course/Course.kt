package com.app.dayplan.course

import com.app.dayplan.step.PlaceCategory

data class Course(
    val courseId: Long?,
    val groupId: Long = 0,
    val step: Int,
    val placeCategory: PlaceCategory,
    val placeId: Long,
)