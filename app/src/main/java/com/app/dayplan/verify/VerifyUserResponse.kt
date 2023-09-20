package com.app.dayplan.verify

import com.app.dayplan.util.UserAccountStatus

data class VerifyUserResponse(
    val verified: Boolean,
    val userStatus: UserAccountStatus,
    val mandatoryTermsAgreed: Boolean,
)