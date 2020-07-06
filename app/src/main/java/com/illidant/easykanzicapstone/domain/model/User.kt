package com.illidant.easykanzicapstone.domain.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("roles")
    val roles: List<String>,
    @SerializedName("accessToken")
    private val accessToken: String,
    @SerializedName("tokenType")
    private val tokenType: String
) {
    fun getToken() = "$tokenType $accessToken"
}
