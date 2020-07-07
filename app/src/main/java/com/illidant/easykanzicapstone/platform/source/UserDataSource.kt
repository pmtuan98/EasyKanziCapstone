package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.response.SignupResponse
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.SigninRequest
import com.illidant.easykanzicapstone.domain.request.SignupRequest
import retrofit2.Call

interface UserDataSource {
    interface Local {
        fun saveToken(token: String)
    }

    interface Remote {
        fun login(request: SigninRequest): Call<User>
        fun register(request: SignupRequest) : Call<SignupResponse>
    }
}
