package com.app.dayplan.step

import com.app.dayplan.userlocation.Coordinates

data class CourseSettingApiRequest(
    val groupId: Long = 0,
    val step: Int,
    val placeId: Long,
    val location: Coordinates,
)