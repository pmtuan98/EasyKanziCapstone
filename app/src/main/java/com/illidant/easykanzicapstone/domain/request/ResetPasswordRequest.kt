package com.illidant.easykanzicapstone.domain.request

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest (
    @SerializedName("username")
    val username: String,
    @SerializedName("newPassword")
    val newPassword: String,
    @SerializedName("otpCode")
    val otpCode: String
)