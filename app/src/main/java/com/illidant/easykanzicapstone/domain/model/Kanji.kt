package com.illidant.easykanzicapstone.domain.model

import com.google.gson.annotations.SerializedName

data class Kanji (
    @SerializedName("id")
     val id: Int,
    @SerializedName("word")
     val word: String,
    @SerializedName("vietMean")
     val vietMean: String,
    @SerializedName("chinaMean")
     val chinaMean: String,
    @SerializedName("onyomi")
     val onyomi: String,
    @SerializedName("romajiOnyomi")
     val romajiOnyomi: String,
    @SerializedName("kunyomi")
     val kunyomi: String,
    @SerializedName("romajiKunyomi")
     val romajiKunyomi: String,
    @SerializedName("strokeCount")
     val strokeCount: Int,
    @SerializedName("example")
     val example: String

)