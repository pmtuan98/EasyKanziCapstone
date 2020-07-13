package com.illidant.easykanzicapstone.domain.model

import com.google.gson.annotations.SerializedName

data class Lesson (
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String

)
