package com.app.dayplan.datecourse

data class Location(
    val name: String,
    val code: Long,
) {
    companion object {
        const val DEFAULT_CITY_NAME = "SEOUL"
        const val DEFAULT_CITY_CODE = 11L

        const val DEFAULT_DISTRICT_NAME = "SEOUL_JONGNO"
        const val DEFAULT_DISTRICT_CODE = 11010L
    }
}