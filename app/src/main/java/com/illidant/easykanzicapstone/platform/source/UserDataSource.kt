package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.LoginRequest
import retrofit2.Call

interface UserDataSource {
    interface Local {
        fun saveToken(token: String)
    }

    interface Remote {
        fun login(request: LoginRequest): Call<User>
    }
}
