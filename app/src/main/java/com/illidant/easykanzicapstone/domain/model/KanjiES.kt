package com.illidant.easykanzicapstone.domain.model

import com.google.gson.annotations.SerializedName

data class KanjiES(
    @SerializedName("id")
    val id: Int,
    @SerializedName("kanji")
    val kanji: String,
    @SerializedName("sinoVietnamese")
    val sinoVietnamese: String,
    @SerializedName("kanjiMeaning")
    val kanjiMeaning: String,
    @SerializedName("onyomi")
    val onyomi: String,
    @SerializedName("kunyomi")
    val kunyomi: String,
    @SerializedName("level")
    val level: String
)