package com.illidant.easykanzicapstone.ui.screen.change_password

import com.illidant.easykanzicapstone.domain.request.ChangePasswordRequest
import com.illidant.easykanzicapstone.domain.response.ChangePasswordResponse
import com.illidant.easykanzicapstone.platform.repository.UserRepositoryType
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChangePassPresenter(
    private val view: ChangePassContract.View,
    private val repository: UserRepositoryType
) : ChangePassContract.Presenter {
    override fun changePass(request: ChangePasswordRequest) {
        repository.changePass(request).enqueue(object : Callback<ChangePasswordResponse> {
            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ChangePasswordResponse>,
                response: Response<ChangePasswordResponse>
            ) {
                response.body()?.let {
                    view.onChangePassSucceeded(it.message)
                }
                response.errorBody()?.let {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    view.onChangePassFail(jObjError.getString("message"))
                }
            }

        })
    }

}