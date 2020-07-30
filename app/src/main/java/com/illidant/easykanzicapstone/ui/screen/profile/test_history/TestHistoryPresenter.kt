package com.illidant.easykanzicapstone.ui.screen.profile.test_history

import com.illidant.easykanzicapstone.domain.model.TestHistory
import com.illidant.easykanzicapstone.platform.repository.TestRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestHistoryPresenter(
    private val view : TestHistoryContract.View,
    private val repository: TestRepository) : TestHistoryContract.Presenter {
    override fun getTestHistoryRequest(id: Int) {
       repository.getTestHistoryByUserID(id).enqueue(object : Callback<List<TestHistory>>{

           override fun onFailure(call: Call<List<TestHistory>>, t: Throwable) {

           }

           override fun onResponse(call: Call<List<TestHistory>>, response: Response<List<TestHistory>>) {
               response.body()?.let {
                   view.onTestHistoryData(it)
               }
           }

       })
    }

}