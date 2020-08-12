package com.illidant.easykanzicapstone.ui.screen.test

import com.illidant.easykanzicapstone.domain.request.TestRankingRequest

interface TestContract {
    interface View {
        fun onSendTestResultSucceeded(message: String)

        fun onSendTestResultFail(message: String)
    }

    interface Presenter {
        fun sendTestResult(request: TestRankingRequest)
    }
}