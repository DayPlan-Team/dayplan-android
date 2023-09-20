package com.app.dayplan.util

data class ErrorResponse(
    val status: Int,
    val code: String,
    val message: String,
) {
    companion object {
        fun fromErrorStatus(errorStatus: ErrorStatus): ErrorResponse {
            return ErrorResponse(
                status = errorStatus.status,
                code = errorStatus.errorCode,
                message = errorStatus.message,
            )
        }
    }
}
