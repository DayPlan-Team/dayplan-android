package com.app.dayplan.terms

data class Terms(
    val termsId: Int,
    val sequence: Int,
    val content: String,
    val mandatory: Boolean,
)