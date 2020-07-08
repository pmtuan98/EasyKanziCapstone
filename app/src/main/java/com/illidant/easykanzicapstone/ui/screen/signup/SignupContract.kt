package com.illidant.easykanzicapstone.ui.screen.signup

import com.illidant.easykanzicapstone.domain.request.SignupRequest

interface SignupContract {
    interface View {
        fun onSignupSucceeded(message: String)

        fun onSignupFailed(exception: Throwable)
    }

    interface Presenter {
        fun signup(request: SignupRequest)
    }
}