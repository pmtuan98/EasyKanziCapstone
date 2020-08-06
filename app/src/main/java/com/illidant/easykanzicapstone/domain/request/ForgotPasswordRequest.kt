package com.illidant.easykanzicapstone.domain.request

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest (
    @SerializedName("username")
    val username: String
)