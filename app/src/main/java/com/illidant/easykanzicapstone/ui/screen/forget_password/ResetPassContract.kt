package com.illidant.easykanzicapstone.ui.screen.forget_password

import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.ResetPasswordRequest


interface ResetPassContract {
    interface View {
        fun onResetPassSucceeded(message: String)

        fun onResetPassFail(message: String)
    }

    interface Presenter {
        fun resetPass(request: ResetPasswordRequest)
    }
}