package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.request.SearchRequest
import com.illidant.easykanzicapstone.domain.response.SearchResponse
import retrofit2.Call

interface SearchDataSource {
    interface Remote {
        fun searchKanji(request: SearchRequest): Call<SearchResponse>
    }
}