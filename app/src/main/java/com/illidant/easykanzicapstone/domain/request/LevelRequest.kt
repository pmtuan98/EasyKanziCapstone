package com.illidant.easykanzicapstone.domain.request

import com.google.gson.annotations.SerializedName

data class LevelRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String
)