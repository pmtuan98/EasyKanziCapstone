package com.illidant.easykanzicapstone.ui.screen.forgot_password

import com.illidant.easykanzicapstone.domain.request.ForgotPasswordRequest
import com.illidant.easykanzicapstone.domain.response.ForgotPasswordResponse
import com.illidant.easykanzicapstone.platform.repository.UserRepositoryType
import com.illidant.easykanzicapstone.ui.screen.reset_password.ResetPassContract
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassPresenter (
    private val view : ForgotPassContract.View,
    private val repository: UserRepositoryType
): ForgotPassContract.Presenter {
    override fun forgotPass(request: ForgotPasswordRequest){
        repository.forgotPass(request).enqueue(object : Callback<ForgotPasswordResponse>{
            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                response.body()?.let {
                    view.onForgotPassSucceeded(it.message)
                }
                response.errorBody()?.let {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    view.onForgotPassFail(  jObjError.getString("message"))
                }
            }

        })
    }

}