package com.illidant.easykanzicapstone.domain.request

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String
)

