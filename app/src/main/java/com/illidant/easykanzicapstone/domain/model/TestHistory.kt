package com.illidant.easykanzicapstone.domain.model

import com.google.gson.annotations.SerializedName

data class TestHistory (
    @SerializedName("name")
    val levelName: String,
    @SerializedName("timeTaken")
    val timeTaken: Int,
    @SerializedName("resultPoint")
    val resultPoint: Int,
    @SerializedName("dateAttend")
    val dateAttend: String
)