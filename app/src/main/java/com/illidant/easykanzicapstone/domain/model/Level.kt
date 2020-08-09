package com.illidant.easykanzicapstone.domain.model
import com.google.gson.annotations.SerializedName

data class Level (
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("description")
    val description: String
)