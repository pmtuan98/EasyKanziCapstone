package com.illidant.easykanzicapstone.ui.screen.ranking

import com.illidant.easykanzicapstone.domain.model.TestRanking
import com.illidant.easykanzicapstone.platform.repository.TestRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankingPresenter(
    private val view: RankingContract.View,
    private val repository: TestRepository) : RankingContract.Presenter {
    override fun getRankingByLevelIDRequest(id: Int) {
       repository.getTestRankingByLevelID(id).enqueue(object : Callback<List<TestRanking>>{
           override fun onFailure(call: Call<List<TestRanking>>, t: Throwable) {

           }

           override fun onResponse(call: Call<List<TestRanking>>, response: Response<List<TestRanking>>) {
               response.body()?.let {
                   view.onRankingData(it)
               }
           }

       })
    }

}