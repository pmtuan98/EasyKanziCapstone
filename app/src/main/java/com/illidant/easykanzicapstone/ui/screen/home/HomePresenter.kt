package com.illidant.easykanzicapstone.ui.screen.home

import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.domain.request.LevelRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomePresenter(
    private val repository: HomeActivity
) : HomeContract.Presenter{

    override fun listLevel(request: LevelRequest) {
//        repository.listLevel(request).enqueue(object : Callback<Level> {
//
//            override fun onResponse(call: Call<Level>, response: Response<Level>) {
//                response.body()?.name
//            }
//
//            override fun onFailure(call: Call<Level>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//        })
    }
}