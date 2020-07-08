package com.illidant.easykanzicapstone.ui.screen.signin

import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.SigninRequest
import com.illidant.easykanzicapstone.platform.repository.UserRepositoryType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninPresenter(
    private val view: SigninContract.View,
    private val repository: UserRepositoryType
) : SigninContract.Presenter {

    override fun signin(request: SigninRequest) {
        repository.signin(request).enqueue(object : Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {
                response.body()?.let {
                    repository.saveToken(it.getToken())
                    view.onSigninSucceeded(it)
                }
                response.errorBody()?.let {
                    view.onSigninFailed(Throwable(it.toString()))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                view.onSigninFailed(t)
            }
        })
    }
}
