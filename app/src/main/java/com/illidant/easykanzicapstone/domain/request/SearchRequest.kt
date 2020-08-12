package com.illidant.easykanzicapstone.domain.request

import com.google.gson.annotations.SerializedName

data class SearchRequest(
    @SerializedName("keyword")
    val keyword: String
)