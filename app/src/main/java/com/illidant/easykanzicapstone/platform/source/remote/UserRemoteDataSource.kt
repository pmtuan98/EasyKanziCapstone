package com.illidant.easykanzicapstone.platform.source.remote

import com.illidant.easykanzicapstone.domain.response.SignupResponse
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.SigninRequest
import com.illidant.easykanzicapstone.domain.request.SignupRequest
import com.illidant.easykanzicapstone.platform.api.ApiService
import com.illidant.easykanzicapstone.platform.source.UserDataSource
import retrofit2.Call

class UserRemoteDataSource(
    private val apiService: ApiService
) : UserDataSource.Remote {

    override fun login(request: SigninRequest): Call<User> = apiService.login(request)
    override fun register(request: SignupRequest): Call<SignupResponse> = apiService.register(request)
}
