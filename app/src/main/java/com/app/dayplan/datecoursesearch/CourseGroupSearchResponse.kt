package com.app.dayplan.datecoursesearch

import com.app.dayplan.step.PlaceCategory
import java.time.LocalDateTime

data class CourseGroupSearchResponse(
    val hasNext: Boolean,
    val courseGroupItems: List<CourseGroupItem>,
)

data class CourseGroupItem(
    val title: String,
    val groupId: Long,
    val cityName: String,
    val districtName: String,
    val courseCategories: List<CourseStepItem>,
    val modifiedAt: String,
)

data class CourseStepItem(
    val step: Int,
    val courseId: Long,
    val category: PlaceCategory,
)