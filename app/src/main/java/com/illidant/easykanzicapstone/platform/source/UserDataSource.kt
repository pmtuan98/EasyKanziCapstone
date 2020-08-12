package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.response.SignupResponse
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.*
import com.illidant.easykanzicapstone.domain.response.ChangePasswordResponse
import com.illidant.easykanzicapstone.domain.response.ForgotPasswordResponse
import com.illidant.easykanzicapstone.domain.response.ResetPasswordResponse
import retrofit2.Call

interface UserDataSource {
    interface Local {
        fun saveToken(token: String)
    }

    interface Remote {
        fun signin(request: SigninRequest): Call<User>
        fun signup(request: SignupRequest): Call<SignupResponse>
        fun resetPass(request: ResetPasswordRequest): Call<ResetPasswordResponse>
        fun changePass(request: ChangePasswordRequest): Call<ChangePasswordResponse>
        fun forgotPass(request: ForgotPasswordRequest): Call<ForgotPasswordResponse>
    }
}
