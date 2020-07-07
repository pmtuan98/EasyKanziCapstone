package com.illidant.easykanzicapstone.ui.screen.signup

import com.illidant.easykanzicapstone.domain.request.SignupRequest

interface SignupContract {
    interface View {
        fun onRegisterSucceeded(message: String)

        fun onRegisterFailed(exception: Throwable)
    }

    interface Presenter {
        fun register(request: SignupRequest)
    }
}