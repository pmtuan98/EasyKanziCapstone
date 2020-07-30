package com.illidant.easykanzicapstone.domain.model

import com.google.gson.annotations.SerializedName

data class TestHistory (
    @SerializedName("name")
    val levelName: String,
    @SerializedName("timeTaken")
    val timeTaken: String,
    @SerializedName("resultPoint")
    val resultPoint: String,
    @SerializedName("dateAttend")
    val dateAttend: String
)