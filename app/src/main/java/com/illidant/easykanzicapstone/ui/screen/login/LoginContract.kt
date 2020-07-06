package com.illidant.easykanzicapstone.ui.screen.login

import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.LoginRequest

interface LoginContract {
    interface View {
        fun onLoginSucceeded(user: User)

        fun onLoginFailed(exception: Throwable)
    }

    interface Presenter {
        fun login(request: LoginRequest)
    }
}
