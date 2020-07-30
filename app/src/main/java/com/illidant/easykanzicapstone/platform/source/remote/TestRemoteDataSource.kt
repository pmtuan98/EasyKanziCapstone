package com.illidant.easykanzicapstone.platform.source.remote

import com.illidant.easykanzicapstone.domain.model.TestHistory
import com.illidant.easykanzicapstone.domain.request.TestRankingRequest
import com.illidant.easykanzicapstone.domain.response.TestRankingResponse
import com.illidant.easykanzicapstone.platform.api.ApiService
import com.illidant.easykanzicapstone.platform.source.TestDataSource
import retrofit2.Call

class TestRemoteDataSource (
    private val apiService: ApiService
): TestDataSource.Remote {
    override fun sendTestResult(request: TestRankingRequest): Call<TestRankingResponse> = apiService.sendTestResult(request)
    override fun getTestHistoryByUserID(id: Int): Call<List<TestHistory>> = apiService.getTestHitoryByUserID(id)

}