package com.illidant.easykanzicapstone.ui.screen.signup

import com.illidant.easykanzicapstone.domain.request.SignupRequest

interface SignupContract {
    interface View {
        fun onSignupSucceeded(message: String)

        fun onSignupFailed(message: String)
    }

    interface Presenter {
        fun signup(request: SignupRequest)
    }
}