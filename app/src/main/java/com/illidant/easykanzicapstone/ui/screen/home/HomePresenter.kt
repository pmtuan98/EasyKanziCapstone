package com.illidant.easykanzicapstone.ui.screen.home

import com.illidant.easykanzicapstone.domain.response.LevelResponse
import com.illidant.easykanzicapstone.platform.repository.LevelRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePresenter(
    private val view: HomeContract.View,
    private val repository: LevelRepository
) : HomeContract.Presenter {
    override fun getLevelData() {
        repository.getLevelData().enqueue(object : Callback<LevelResponse> {
            override fun onResponse(call: Call<LevelResponse>, response: Response<LevelResponse>) {
                response.body()?.let {
                    view.onDataComplete(it.body)
                }
            }

            override fun onFailure(call: Call<LevelResponse>, t: Throwable) {

            }
        })

    }

}