package com.illidant.easykanzicapstone.ui.screen.signup

import android.util.Log
import com.illidant.easykanzicapstone.domain.response.SignupResponse
import com.illidant.easykanzicapstone.domain.request.SignupRequest
import com.illidant.easykanzicapstone.platform.repository.UserRepositoryType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupPresenter (
    private val view: SignupContract.View,
    private val repository: UserRepositoryType
) : SignupContract.Presenter {


    override fun signup(request: SignupRequest) {
        repository.signup(request).enqueue(object : Callback<SignupResponse>{

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                view.onSignupFailed(t)
            }

            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                response.body()?.let {
                    Log.d("SignupPresenter",it.message)
                    view.onSignupSucceeded(it.message)
                }
                response.errorBody()?.let {
                    Log.d("SignupPresenter",it.toString())
                    view.onSignupFailed(Throwable(it.toString()))
                }
            }

        })
    }


}

