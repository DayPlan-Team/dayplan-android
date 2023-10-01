package com.app.dayplan.step

import java.io.Serializable

data class Steps(
    val stepNumber: Int,
    val stepCategory: PlaceCategory,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val placeName: String = "",
    val placeAddress: String = "",
) : Serializable