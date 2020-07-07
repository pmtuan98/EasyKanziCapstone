package com.illidant.easykanzicapstone.platform.repository

import com.illidant.easykanzicapstone.domain.response.SignupResponse
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.SigninRequest
import com.illidant.easykanzicapstone.domain.request.SignupRequest
import com.illidant.easykanzicapstone.platform.source.UserDataSource
import retrofit2.Call

interface UserRepositoryType : UserDataSource.Local, UserDataSource.Remote

class UserRepository(
    private val local: UserDataSource.Local,
    private val remote: UserDataSource.Remote
) : UserRepositoryType {

    override fun saveToken(token: String) = local.saveToken(token)

    override fun login(request: SigninRequest): Call<User> = remote.login(request)
    override fun register(request: SignupRequest): Call<SignupResponse> = remote.register(request)
}
