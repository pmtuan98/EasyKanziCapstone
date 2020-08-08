package com.illidant.easykanzicapstone.ui.screen.signin

import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.SigninRequest

interface SigninContract {
    interface View {
        fun onSigninSucceeded(user: User)

        fun onSigninFailed(message: String)
    }

    interface Presenter {
        fun signin(request: SigninRequest)
    }
}
