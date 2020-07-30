package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.request.*
import com.illidant.easykanzicapstone.domain.response.SearchResponse
import retrofit2.Call

interface SearchDataSource {
    interface Local {

    }

    interface Remote {
        fun searchKanji(request: SearchRequest) : Call<SearchResponse>
    }
}