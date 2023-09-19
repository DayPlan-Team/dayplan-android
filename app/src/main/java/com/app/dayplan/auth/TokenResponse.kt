package com.app.dayplan.auth

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)