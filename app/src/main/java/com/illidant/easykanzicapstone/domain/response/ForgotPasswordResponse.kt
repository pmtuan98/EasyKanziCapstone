package com.illidant.easykanzicapstone.domain.response

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("message")
    val message: String
)