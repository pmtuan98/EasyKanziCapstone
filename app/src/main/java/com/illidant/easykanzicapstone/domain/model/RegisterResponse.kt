package com.illidant.easykanzicapstone.domain.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @SerializedName("message")
    val message: String
)
