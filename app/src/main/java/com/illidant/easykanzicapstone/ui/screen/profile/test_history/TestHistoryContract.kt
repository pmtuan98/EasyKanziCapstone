package com.illidant.easykanzicapstone.ui.screen.profile.test_history

import com.illidant.easykanzicapstone.domain.model.TestHistory

interface TestHistoryContract {
    interface View {
        fun onTestHistoryData(listHistory: List<TestHistory>)
    }

    interface Presenter {
        fun getTestHistoryRequest(userId: Int, levelId:Int)
    }
}