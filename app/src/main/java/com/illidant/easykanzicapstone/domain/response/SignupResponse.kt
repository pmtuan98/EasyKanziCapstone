package com.illidant.easykanzicapstone.domain.response

import com.google.gson.annotations.SerializedName

data class SignupResponse (
    @SerializedName("message")
    val message: String
)
