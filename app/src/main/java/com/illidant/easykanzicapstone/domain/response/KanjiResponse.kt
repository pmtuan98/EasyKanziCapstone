package com.illidant.easykanzicapstone.domain.response

import com.google.gson.annotations.SerializedName
import com.illidant.easykanzicapstone.domain.model.Kanji

data class KanjiResponse(
    @SerializedName("errorcode")
    val errorcode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("body")
    val body: Kanji
)