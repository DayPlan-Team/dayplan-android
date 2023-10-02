package com.app.dayplan.step

import java.io.Serializable

data class PlaceItemApiOuterResponse(
    val total: Int = 0,
    val start: Int = 1,
    val display: Int = 10,
    val items: List<PlaceItemApiResponse> = emptyList(),
)

data class PlaceItemApiResponse(
    val title: String = "",
    val link: String = "",
    val category: String = "",
    val description: String = "",
    val telephone: String = "",
    val address: String = "",
    val roadAddress: String = "",
    val mapx: String = "",
    val mapy: String = "",
) : Serializable