package com.illidant.easykanzicapstone.domain.request

import com.google.gson.annotations.SerializedName

data class TestRankingRequest (
    @SerializedName("dateAttend")
    val dateAttend: String,
    @SerializedName("level_id")
    val level_id: Int,
    @SerializedName("resultPoint")
    val resultPoint: Int,
    @SerializedName("timeTaken")
    val timeTaken: Int,
    @SerializedName("user_id")
    val user_id: Int
)