package com.app.dayplan.step

import java.io.Serializable

data class Steps(
    val stepNumber: Int,
    val stepCategory: PlaceCategory,
    val placeName: String = "",
    val stage: StepStage = StepStage.START,
) : Serializable
