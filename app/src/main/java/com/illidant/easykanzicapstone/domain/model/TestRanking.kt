package com.illidant.easykanzicapstone.domain.model

import com.google.gson.annotations.SerializedName

data class TestRanking(
    @SerializedName("timeTaken")
    val timeTaken: String,
    @SerializedName("resultPoint")
    val resultPoint: String,
    @SerializedName("dateAttend")
    val dateAttend: String,
    @SerializedName("userName")
    val userName: String
)