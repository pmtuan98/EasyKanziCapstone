package com.illidant.easykanzicapstone.domain.model

import com.google.gson.annotations.SerializedName

data class Vocabulary(
    @SerializedName("id")
    val id: Int,
    @SerializedName("hiragana")
    val hiragana: String,
    @SerializedName("kanji_vocab")
    val kanji_vocab: String,
    @SerializedName("vocab_meaning")
    val vocab_meaning: String
)