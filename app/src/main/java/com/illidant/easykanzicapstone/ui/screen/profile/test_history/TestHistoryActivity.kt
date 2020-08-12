package com.illidant.easykanzicapstone.ui.screen.profile.test_history

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.TestHistory
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.TestRepository
import com.illidant.easykanzicapstone.platform.source.remote.TestRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.home.HomeActivity
import com.illidant.easykanzicapstone.ui.screen.ranking.RankingActivity
import com.illidant.easykanzicapstone.ui.screen.search.SearchActivity
import kotlinx.android.synthetic.main.activity_test_history.*

class TestHistoryActivity : AppCompatActivity(), TestHistoryContract.View {
    private var levelId:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_history)
        initialize()
        configViews()
    }
    private fun initialize() {
        val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        val userID = prefs.getInt("userID", 0)
        historyPresenter.getTestHistoryRequest(userID,1)
        btnJPD111.background =  ContextCompat.getDrawable(this, R.drawable.btn_mess)
    }
    private fun changeButtonColor() {
        if(btnJPD111.isPressed) {
            btnJPD111.background =  ContextCompat.getDrawable(this, R.drawable.btn_mess)
            btnJPD121.background =  ContextCompat.getDrawable(this, R.drawable.btn_mess2)
            btnJPD131.background =  ContextCompat.getDrawable(this, R.drawable.btn_mess2)
        }else if (btnJPD121.isPressed){
            btnJPD111.background =  ContextCompat.getDrawable(this, R.drawable.btn_mess2)
            btnJPD121.background =  ContextCompat.getDrawable(this, R.drawable.btn_mess)
            btnJPD131.background =  ContextCompat.getDrawable(this, R.drawable.btn_mess2)
        }else if (btnJPD131.isPressed){
            btnJPD121.background =  ContextCompat.getDrawable(this, R.drawable.btn_mess2)
            btnJPD111.background =  ContextCompat.getDrawable(this, R.drawable.btn_mess2)
            btnJPD131.background =  ContextCompat.getDrawable(this, R.drawable.btn_mess)
        }
    }

    private fun configViews() {
        val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        val userID = prefs.getInt("userID", 0)
        btnJPD111.setOnClickListener {
            changeButtonColor()
            levelId = 1
            historyPresenter.getTestHistoryRequest(userID,levelId)
        }

        btnJPD121.setOnClickListener {
            changeButtonColor()
            levelId = 2
            historyPresenter.getTestHistoryRequest(userID,levelId)
        }

        btnJPD131.setOnClickListener {
            changeButtonColor()
            levelId = 3
            historyPresenter.getTestHistoryRequest(userID,levelId)
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
        recyclerViewHistory!!.layoutManager = GridLayoutManager(this, 1)
        recyclerViewHistory!!.adapter = TestHistoryAdapter(listHistory, this)
    }

}