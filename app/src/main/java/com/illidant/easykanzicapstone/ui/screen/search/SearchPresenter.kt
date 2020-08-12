package com.illidant.easykanzicapstone.ui.screen.search


import android.util.Log
import com.illidant.easykanzicapstone.domain.request.SearchRequest
import com.illidant.easykanzicapstone.domain.response.SearchResponse
import com.illidant.easykanzicapstone.platform.repository.SearchRepositoryType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPresenter(
    private val view: SearchContract.View,
    private val repository: SearchRepositoryType
) : SearchContract.Presenter {
    override fun searchKanji(request: SearchRequest) {
        repository.searchKanji(request).enqueue(object : Callback<SearchResponse> {

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<SearchResponse>, response: Response<SearchResponse>
            ) {
                response.body()?.let {
                    view.onSearchResult(it.content)
                }
            }

        })
    }

}