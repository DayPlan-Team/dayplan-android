package com.app.dayplan.datecourse

data class Location(
    val name: String,
    val code: Long,
) {
    companion object {
        const val DEFAULT_CITY_NAME = "SEOUL"
        const val DEFAULT_CITY_CODE = 11L
    }
}