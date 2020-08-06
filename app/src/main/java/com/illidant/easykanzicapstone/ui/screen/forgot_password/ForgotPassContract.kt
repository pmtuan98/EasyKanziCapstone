package com.illidant.easykanzicapstone.ui.screen.forgot_password

import com.illidant.easykanzicapstone.domain.request.ForgotPasswordRequest
import com.illidant.easykanzicapstone.domain.request.ResetPasswordRequest

interface ForgotPassContract {
    interface View {
        fun onForgotPassSucceeded(message: String)

        fun onForgotPassFail(message: String)
    }

    interface Presenter {
        fun forgotPass(request: ForgotPasswordRequest)
    }

}