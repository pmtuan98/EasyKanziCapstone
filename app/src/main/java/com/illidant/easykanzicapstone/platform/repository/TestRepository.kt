package com.illidant.easykanzicapstone.platform.repository

import com.illidant.easykanzicapstone.domain.request.TestRankingRequest
import com.illidant.easykanzicapstone.domain.response.TestRankingResponse
import com.illidant.easykanzicapstone.platform.source.TestDataSource
import retrofit2.Call

interface TestRepositoryType : TestDataSource.Remote
class TestRepository (
    private val remote: TestDataSource.Remote
): TestRepositoryType {
    override fun sendTestResult(request: TestRankingRequest): Call<TestRankingResponse> = remote.sendTestResult(request)
}