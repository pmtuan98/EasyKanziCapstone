package com.illidant.easykanzicapstone.ui.screen.profile.test_history

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.illidant.easykanzicapstone.BaseActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.TestHistory
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.TestRepository
import com.illidant.easykanzicapstone.platform.source.remote.TestRemoteDataSource
import kotlinx.android.synthetic.main.activity_test_history.*

class TestHistoryActivity : BaseActivity(), TestHistoryContract.View {
    private var levelId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_history)
        initialize()
        configViews()
    }

    private fun initialize() {
        val prefs: SharedPreferences =
            getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        val userID = prefs.getInt("userID", 0)
        historyPresenter.getTestHistoryRequest(userID, 1)
        btnJPD111.background = ContextCompat.getDrawable(this, R.drawable.btn_ranking_pressed)
    }

    private fun changeButtonColor() {
        if (btnJPD111.isPressed) {
            btnJPD111.background = ContextCompat.getDrawable(this, R.drawable.btn_ranking_pressed)
            btnJPD111.setTextColor(Color.BLACK)
            btnJPD121.background = ContextCompat.getDrawable(this, R.drawable.btn_ranking_default)
            btnJPD121.setTextColor(Color.GRAY)
            btnJPD131.background = ContextCompat.getDrawable(this, R.drawable.btn_ranking_default)
            btnJPD131.setTextColor(Color.GRAY)
        } else if (btnJPD121.isPressed) {
            btnJPD111.background = ContextCompat.getDrawable(this, R.drawable.btn_ranking_default)
            btnJPD111.setTextColor(Color.GRAY)
            btnJPD121.background = ContextCompat.getDrawable(this, R.drawable.btn_ranking_pressed)
            btnJPD121.setTextColor(Color.BLACK)
            btnJPD131.background = ContextCompat.getDrawable(this, R.drawable.btn_ranking_default)
            btnJPD131.setTextColor(Color.GRAY)
        } else if (btnJPD131.isPressed) {
            btnJPD111.background = ContextCompat.getDrawable(this, R.drawable.btn_ranking_default)
            btnJPD111.setTextColor(Color.GRAY)
            btnJPD121.background = ContextCompat.getDrawable(this, R.drawable.btn_ranking_default)
            btnJPD121.setTextColor(Color.GRAY)
            btnJPD131.background = ContextCompat.getDrawable(this, R.drawable.btn_ranking_pressed)
            btnJPD131.setTextColor(Color.BLACK)
        }
    }

    private fun configViews() {
        val prefs: SharedPreferences =
            getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        val userID = prefs.getInt("userID", 0)
        btnJPD111.setOnClickListener {
            changeButtonColor()
            levelId = 1
            historyPresenter.getTestHistoryRequest(userID, levelId)
        }

        btnJPD121.setOnClickListener {
            changeButtonColor()
            levelId = 2
            historyPresenter.getTestHistoryRequest(userID, levelId)
        }

        btnJPD131.setOnClickListener {
            changeButtonColor()
            levelId = 3
            historyPresenter.getTestHistoryRequest(userID, levelId)
        }
        btnBack.setOnClickListener {
            finish()
        }
    }

    private val historyPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = TestRemoteDataSource(retrofit)
        val repository = TestRepository(remote)
        TestHistoryPresenter(this, repository)
    }

    override fun onTestHistoryData(listHistory: List<TestHistory>) {
        if (listHistory.size == 0) {
            tvNotDone.visibility = View.VISIBLE
        } else {
            tvNotDone.visibility = View.INVISIBLE
        }
        recyclerViewHistory!!.layoutManager = GridLayoutManager(this, 1)
        recyclerViewHistory!!.adapter = TestHistoryAdapter(listHistory, this)
    }

}