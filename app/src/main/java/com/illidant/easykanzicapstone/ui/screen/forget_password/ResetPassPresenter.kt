package com.illidant.easykanzicapstone.ui.screen.forget_password

import com.illidant.easykanzicapstone.domain.request.ResetPasswordRequest
import com.illidant.easykanzicapstone.domain.response.ResetPasswordResponse
import com.illidant.easykanzicapstone.platform.repository.UserRepositoryType
import com.illidant.easykanzicapstone.ui.screen.signin.SigninContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPassPresenter (
    private val view : ResetPassContract.View,
    private val repository: UserRepositoryType
): ResetPassContract.Presenter {
    override fun resetPass(request: ResetPasswordRequest) {
        repository.resetPass(request).enqueue(object : Callback<ResetPasswordResponse> {

            override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {
                response.body()?.let {
                    view.onResetPassSucceeded(it.message)
                }
                response.errorBody()?.let {
                    view.onResetPassFail(it.toString())
                }
            }
        })
    }

}

