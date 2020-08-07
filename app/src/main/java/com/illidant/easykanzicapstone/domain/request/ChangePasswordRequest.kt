package com.illidant.easykanzicapstone.domain.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest (
    @SerializedName("email")
    val email: String,
    @SerializedName("newPassword")
    val newPassword: String,
    @SerializedName("currentPassword")
    val currentPassword: String
)