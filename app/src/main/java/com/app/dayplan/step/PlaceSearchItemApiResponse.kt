package com.app.dayplan.step

import java.io.Serializable

data class PlaceSearchItemApiResponse(
    val items: List<PlaceItemApiResponse> = emptyList(),
)

data class PlaceItemApiResponse(
    val placeId: Long = 0L,
    val title: String = "",
    val link: String = "",
    val category: String = "",
    val description: String = "",
    val telephone: String = "",
    val address: String = "",
    val roadAddress: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
) : Serializable