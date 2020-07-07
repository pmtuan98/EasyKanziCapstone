package com.illidant.easykanzicapstone.ui.screen.register

import com.illidant.easykanzicapstone.domain.request.RegisterRequest

interface RegisterContract {
    interface View {
        fun onRegisterSucceeded(message: String)

        fun onRegisterFailed(exception: Throwable)
    }

    interface Presenter {
        fun register(request: RegisterRequest)
    }
}