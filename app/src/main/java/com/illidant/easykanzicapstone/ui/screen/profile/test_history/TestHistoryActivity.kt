package com.illidant.easykanzicapstone.ui.screen.profile.test_history

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.TestHistory
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.SearchRepository
import com.illidant.easykanzicapstone.platform.repository.TestRepository
import com.illidant.easykanzicapstone.platform.source.remote.SearchRemoteDataSource
import com.illidant.easykanzicapstone.platform.source.remote.TestRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.search.SearchAdapter
import com.illidant.easykanzicapstone.ui.screen.search.SearchPresenter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_test_history.*

class TestHistoryActivity : AppCompatActivity(), TestHistoryContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_history)
        val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        val userID = prefs.getInt("userID", 0)
        historyPresenter.getTestHistoryRequest(userID)
    }

    private val historyPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = TestRemoteDataSource(retrofit)
        val repository = TestRepository(remote)
        TestHistoryPresenter(this, repository)
    }

    override fun onTestHistoryData(listHistory: List<TestHistory>) {
        recyclerHistory!!.layoutManager = GridLayoutManager(this, 1)
        recyclerHistory!!.adapter = TestHistoryAdapter(listHistory,this)
    }
}