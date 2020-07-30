package com.illidant.easykanzicapstone.domain.model

import com.google.gson.annotations.SerializedName

data class Test (
    @SerializedName("dateAttend")
    val dateAttend: String,
    @SerializedName("level_id")
    val level_id: Int,
    @SerializedName("resultPoint")
    val resultPoint: String,
    @SerializedName("timeTaken")
    val timeTaken: String,
    @SerializedName("user_id")
    val user_id: Int
)