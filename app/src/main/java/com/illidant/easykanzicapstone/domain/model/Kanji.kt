package com.illidant.easykanzicapstone.domain.model

import com.google.gson.annotations.SerializedName

data class Kanji(
    @SerializedName("id")
    val id: Int,
    @SerializedName("kanji")
    val name: String,
    @SerializedName("sino_vietnamese")
    val image: String,
    @SerializedName("meaning")
    val meaning: String,
    @SerializedName("onyomi")
    val onyomi: String,
    @SerializedName("on_furigana")
    val on_furigana: String,
    @SerializedName("kunyomi")
    val kunyomi: String,
    @SerializedName("kun_furigana")
    val kun_furigana: String
)
