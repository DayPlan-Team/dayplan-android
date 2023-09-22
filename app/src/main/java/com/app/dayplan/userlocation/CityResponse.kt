package com.app.dayplan.userlocation

data class CityResponse(
    val name: String,
    val code: Long,
) {
    companion object {
        const val DEFAULT_NAME = "SEOUL"
        const val DEFAULT_CODE = 11L
    }
}