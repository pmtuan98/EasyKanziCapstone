package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.model.RegisterResponse
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.LoginRequest
import com.illidant.easykanzicapstone.domain.request.RegisterRequest
import retrofit2.Call

interface UserDataSource {
    interface Local {
        fun saveToken(token: String)
    }

    interface Remote {
        fun login(request: LoginRequest): Call<User>
        fun register(request: RegisterRequest) : Call<RegisterResponse>
    }
}
