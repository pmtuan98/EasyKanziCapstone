package com.illidant.easykanzicapstone.domain.response

import com.google.gson.annotations.SerializedName
import com.illidant.easykanzicapstone.domain.model.KanjiES

data class SearchResponse(
    @SerializedName("content")
    val content: List<KanjiES>
)