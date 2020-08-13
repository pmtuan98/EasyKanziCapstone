package com.illidant.easykanzicapstone.platform.source.remote

import com.illidant.easykanzicapstone.domain.request.SearchRequest
import com.illidant.easykanzicapstone.domain.response.SearchResponse
import com.illidant.easykanzicapstone.platform.api.ApiService
import com.illidant.easykanzicapstone.platform.source.SearchDataSource
import retrofit2.Call

class SearchRemoteDataSource(
    private val apiService: ApiService
) : SearchDataSource.Remote {
    override fun searchKanji(request: SearchRequest): Call<SearchResponse> =
        apiService.searchKanji(request)
}