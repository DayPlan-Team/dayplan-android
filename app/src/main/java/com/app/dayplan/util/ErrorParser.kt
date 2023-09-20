package com.app.dayplan.util

import com.google.gson.Gson
import okhttp3.ResponseBody

object ErrorParser {
    fun parseResponse(response: ResponseBody?, defaultErrorResponse: ErrorResponse): ErrorResponse {
        val gson = Gson()
        val errorString = response?.string()
        if (errorString != null) {
            return gson.fromJson(errorString, ErrorResponse::class.java)
        }

        return defaultErrorResponse
    }
}