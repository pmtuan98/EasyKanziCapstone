package com.illidant.easykanzicapstone.platform.api

import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.LoginRequest
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST(ApiConstant.URL_LOGIN)
    fun login(@Body body: LoginRequest): Call<User>
}
