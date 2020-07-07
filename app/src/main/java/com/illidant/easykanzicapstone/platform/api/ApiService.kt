package com.illidant.easykanzicapstone.platform.api

import com.illidant.easykanzicapstone.domain.response.SignupResponse
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.SigninRequest
import com.illidant.easykanzicapstone.domain.request.SignupRequest
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST(ApiConstant.URL_LOGIN)
    fun login(@Body body: SigninRequest): Call<User>
    @POST(ApiConstant.URL_REGISTER)
    fun register(@Body body: SignupRequest): Call<SignupResponse>
}
