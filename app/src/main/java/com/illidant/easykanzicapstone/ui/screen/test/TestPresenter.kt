package com.illidant.easykanzicapstone.ui.screen.test

import com.illidant.easykanzicapstone.domain.request.TestRankingRequest
import com.illidant.easykanzicapstone.domain.response.TestRankingResponse
import com.illidant.easykanzicapstone.platform.repository.TestRepositoryType
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestPresenter(
    private val view: TestContract.View,
    private val repository: TestRepositoryType
) : TestContract.Presenter {
    override fun sendTestResult(request: TestRankingRequest) {
        repository.sendTestResult(request).enqueue(object : Callback<TestRankingResponse> {

            override fun onFailure(call: Call<TestRankingResponse>, t: Throwable) {
                //Not use
            }

            override fun onResponse(
                call: Call<TestRankingResponse>,
                response: Response<TestRankingResponse>
            ) {
                response.body()?.let {
                    view.onSendTestResultSucceeded(it.message)
                }
                response.errorBody()?.let {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    view.onSendTestResultFail(jObjError.getString("message"))
                }
            }

        })
    }

}