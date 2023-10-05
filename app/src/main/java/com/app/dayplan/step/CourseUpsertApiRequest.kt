package com.app.dayplan.step

import com.app.dayplan.userlocation.Coordinates

data class CourseUpsertApiRequest(
    val courseId: Long = 0,
    val groupId: Long = 0,
    val step: Int,
    val placeCategory: PlaceCategory,
    val placeId: Long,
)