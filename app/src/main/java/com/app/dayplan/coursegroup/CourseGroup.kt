package com.app.dayplan.coursegroup

import java.io.Serializable

data class CourseGroup(
    val groupId: Long,
    val groupName: String,
    val cityCode: Long,
    val cityName: String,
    val districtCode: Long,
    val districtName: String,
) : Serializable

data class CourseGroups(
    val courseGroups: List<CourseGroup>,
)