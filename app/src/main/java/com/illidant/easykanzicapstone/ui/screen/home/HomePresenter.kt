package com.illidant.easykanzicapstone.ui.screen.home

import android.util.Log
import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.platform.repository.LevelRepository
import com.illidant.easykanzicapstone.platform.repository.UserRepositoryType
import com.illidant.easykanzicapstone.ui.screen.signin.SigninContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePresenter(
    private val view: HomeContract.View,
    private val repository: LevelRepository
): HomeContract.Presenter
{
    override fun getLevelDate(){
        repository.getLevelData().enqueue(object : Callback<List<Level>> {
            override fun onResponse(call: Call<List<Level>>, response: Response<List<Level>>) {
                response.body()?.let { view.onDataComplete(it) }
            }

            override fun onFailure(call: Call<List<Level>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }

}