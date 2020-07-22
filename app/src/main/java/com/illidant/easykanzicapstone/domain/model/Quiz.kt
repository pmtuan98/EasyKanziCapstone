package com.illidant.easykanzicapstone.domain.model

import com.google.gson.annotations.SerializedName

data class Quiz (
    @SerializedName("question")
    val question: String,
    @SerializedName("answerA")
    val answerA: String,
    @SerializedName("answerB")
    val answerB: String,
    @SerializedName("answerC")
    val answerC: String,
    @SerializedName("answerD")
    val answerD: String,
    @SerializedName("correctAnswer")
    val correctAnswer: String
)