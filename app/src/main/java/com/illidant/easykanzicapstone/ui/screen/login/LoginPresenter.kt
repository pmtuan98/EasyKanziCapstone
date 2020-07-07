package com.illidant.easykanzicapstone.ui.screen.login

import cn.pedant.SweetAlert.SweetAlertDialog
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.LoginRequest
import com.illidant.easykanzicapstone.platform.repository.UserRepositoryType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(
    private val view: LoginContract.View,
    private val repository: UserRepositoryType
) : LoginContract.Presenter {

    override fun login(request: LoginRequest) {
        repository.login(request).enqueue(object : Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {
                response.body()?.let {
                    repository.saveToken(it.getToken())
                    view.onLoginSucceeded(it)
                }
                response.errorBody()?.let {
                    view.onLoginFailed(Throwable(it.toString()))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                view.onLoginFailed(t)
            }
        })
    }
}
