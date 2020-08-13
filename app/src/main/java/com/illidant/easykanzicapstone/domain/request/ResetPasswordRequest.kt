package com.illidant.easykanzicapstone.domain.request

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("newPassword")
    val newPassword: String,
    @SerializedName("otpCode")
    val otpCode: String
)