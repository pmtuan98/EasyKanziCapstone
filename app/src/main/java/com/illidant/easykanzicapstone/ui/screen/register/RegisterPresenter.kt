package com.illidant.easykanzicapstone.ui.screen.register

import com.illidant.easykanzicapstone.domain.model.RegisterResponse
import com.illidant.easykanzicapstone.domain.request.RegisterRequest
import com.illidant.easykanzicapstone.platform.repository.UserRepositoryType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterPresenter (
    private val view: RegisterContract.View,
    private val repository: UserRepositoryType
) : RegisterContract.Presenter {


    override fun register(request: RegisterRequest) {
        repository.register(request).enqueue(object : Callback<RegisterResponse>{
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                view.onRegisterFailed(t)
            }

            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                response.body()?.let {
                    view.onRegisterSucceeded(it.message.toString())
                }
            }

        })
    }


}

