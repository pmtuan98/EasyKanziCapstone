package com.illidant.easykanzicapstone.platform.repository

import com.illidant.easykanzicapstone.domain.model.RegisterResponse
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.LoginRequest
import com.illidant.easykanzicapstone.domain.request.RegisterRequest
import com.illidant.easykanzicapstone.platform.source.UserDataSource
import retrofit2.Call

interface UserRepositoryType : UserDataSource.Local, UserDataSource.Remote

class UserRepository(
    private val local: UserDataSource.Local,
    private val remote: UserDataSource.Remote
) : UserRepositoryType {

    override fun saveToken(token: String) = local.saveToken(token)

    override fun login(request: LoginRequest): Call<User> = remote.login(request)
    override fun register(request: RegisterRequest): Call<RegisterResponse> = remote.register(request)
}
