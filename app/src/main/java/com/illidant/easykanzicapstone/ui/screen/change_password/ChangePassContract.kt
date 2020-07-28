package com.illidant.easykanzicapstone.ui.screen.change_password

import com.illidant.easykanzicapstone.domain.request.ChangePasswordRequest


interface ChangePassContract {
    interface View {
        fun onChangePassSucceeded(message: String)

        fun onChangePassFail(message: String)
    }

    interface Presenter {
        fun changePass(request: ChangePasswordRequest)
    }
}