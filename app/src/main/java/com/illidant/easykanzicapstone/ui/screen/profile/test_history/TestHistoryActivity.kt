package com.illidant.easykanzicapstone.ui.screen.profile.test_history

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_history)
        val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        val userID = prefs.getInt("userID", 0)
        configViews()
        historyPresenter.getTestHistoryRequest(userID)
    }

    private val historyPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = TestRemoteDataSource(retrofit)
        val repository = TestRepository(remote)
        TestHistoryPresenter(this, repository)
    }

    override fun onTestHistoryData(listHistory: List<TestHistory>) {
        recyclerViewHistory!!.layoutManager = GridLayoutManager(this, 1)
        recyclerViewHistory!!.adapter = TestHistoryAdapter(listHistory,this)
    }

    private fun configViews() {
        //set bottom navigation bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavBar)


        //set home selected
        bottomNavigationView.selectedItemId =
            R.id.profile


        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true

                }
                R.id.search -> {
                    startActivity(Intent(applicationContext, SearchActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true

                }
                R.id.ranking -> {
                    startActivity(Intent(applicationContext, RankingActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true

                }
                R.id.profile -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }
}