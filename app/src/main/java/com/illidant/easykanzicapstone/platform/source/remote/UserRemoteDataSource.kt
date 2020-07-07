package com.illidant.easykanzicapstone.platform.source.remote

import com.illidant.easykanzicapstone.domain.model.RegisterResponse
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.LoginRequest
import com.illidant.easykanzicapstone.domain.request.RegisterRequest
import com.illidant.easykanzicapstone.platform.api.ApiService
import com.illidant.easykanzicapstone.platform.source.UserDataSource
import retrofit2.Call

class UserRemoteDataSource(
    private val apiService: ApiService
) : UserDataSource.Remote {

    override fun login(request: LoginRequest): Call<User> = apiService.login(request)
    override fun register(request: RegisterRequest): Call<RegisterResponse> = apiService.register(request)
}
