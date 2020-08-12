package com.illidant.easykanzicapstone.platform.repository

import com.illidant.easykanzicapstone.domain.request.SearchRequest
import com.illidant.easykanzicapstone.domain.response.SearchResponse
import com.illidant.easykanzicapstone.platform.source.SearchDataSource
import retrofit2.Call

interface SearchRepositoryType : SearchDataSource.Remote

class SearchRepository(
    private val remote: SearchDataSource.Remote
) : SearchRepositoryType {
    override fun searchKanji(request: SearchRequest): Call<SearchResponse> =
        remote.searchKanji(request)

}