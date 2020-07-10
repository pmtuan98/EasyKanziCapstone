package com.illidant.easykanzicapstone.domain.model

import com.google.gson.annotations.SerializedName

data class Example(
    @SerializedName("kanji")
    val kanji: String,
    @SerializedName("hira")
    val hira: String,
    @SerializedName("vn")
    val vn: String
)
