package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.response.SignupResponse
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.ResetPasswordRequest
import com.illidant.easykanzicapstone.domain.request.SigninRequest
import com.illidant.easykanzicapstone.domain.request.SignupRequest
import com.illidant.easykanzicapstone.domain.response.ResetPasswordResponse
import retrofit2.Call

interface UserDataSource {
    interface Local {
        fun saveToken(token: String)
    }

    interface Remote {
        fun signin(request: SigninRequest): Call<User>
        fun signup(request: SignupRequest) : Call<SignupResponse>
        fun resetPass(request: ResetPasswordRequest) : Call<ResetPasswordResponse>
    }
}
