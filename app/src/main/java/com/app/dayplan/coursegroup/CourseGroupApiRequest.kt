package com.app.dayplan.coursegroup

data class CourseGroupApiRequest(
    val groupId: Long = 0L,
    val groupName: String = "",
    val cityCode: Long,
    val districtCode: Long,
)