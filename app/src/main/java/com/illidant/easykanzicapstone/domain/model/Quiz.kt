package com.illidant.easykanzicapstone.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Quiz(
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
) : Parcelable