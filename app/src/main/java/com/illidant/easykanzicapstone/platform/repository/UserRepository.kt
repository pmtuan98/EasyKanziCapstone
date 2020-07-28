package com.illidant.easykanzicapstone.platform.repository

import com.illidant.easykanzicapstone.domain.response.SignupResponse
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.ChangePasswordRequest
import com.illidant.easykanzicapstone.domain.request.ResetPasswordRequest
import com.illidant.easykanzicapstone.domain.request.SigninRequest
import com.illidant.easykanzicapstone.domain.request.SignupRequest
import com.illidant.easykanzicapstone.domain.response.ChangePasswordResponse
import com.illidant.easykanzicapstone.domain.response.ResetPasswordResponse
import com.illidant.easykanzicapstone.platform.source.UserDataSource
import retrofit2.Call

interface UserRepositoryType : UserDataSource.Local, UserDataSource.Remote

class UserRepository(
    private val local: UserDataSource.Local,
    private val remote: UserDataSource.Remote
) : UserRepositoryType {

    override fun saveToken(token: String) = local.saveToken(token)

    override fun signin(request: SigninRequest): Call<User> = remote.signin(request)
    override fun signup(request: SignupRequest): Call<SignupResponse> = remote.signup(request)
    override fun resetPass(request: ResetPasswordRequest): Call<ResetPasswordResponse> = remote.resetPass(request)
    override fun changePass(request: ChangePasswordRequest): Call<ChangePasswordResponse> = remote.changePass(request)

}
