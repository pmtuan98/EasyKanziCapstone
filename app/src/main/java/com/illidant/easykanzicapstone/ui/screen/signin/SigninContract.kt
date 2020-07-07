package com.illidant.easykanzicapstone.ui.screen.signin

import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.SigninRequest

interface SigninContract {
    interface View {
        fun onLoginSucceeded(user: User)

        fun onLoginFailed(exception: Throwable)
    }

    interface Presenter {
        fun login(request: SigninRequest)
    }
}
