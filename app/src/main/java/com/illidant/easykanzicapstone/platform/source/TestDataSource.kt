package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.model.TestHistory
import com.illidant.easykanzicapstone.domain.model.TestRanking
import com.illidant.easykanzicapstone.domain.request.*
import com.illidant.easykanzicapstone.domain.response.TestRankingResponse
import retrofit2.Call

interface TestDataSource {
    interface Local {

    }
    interface Remote {
        fun sendTestResult(request: TestRankingRequest): Call<TestRankingResponse>
        fun getTestHistoryByUserID(id: Int): Call<List<TestHistory>>
        fun getTestRankingByLevelID(id: Int): Call<List<TestRanking>>
    }
}