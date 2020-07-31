package com.illidant.easykanzicapstone.ui.screen.ranking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Lesson
import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.domain.model.TestRanking
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.LevelRepository
import com.illidant.easykanzicapstone.platform.repository.TestRepository
import com.illidant.easykanzicapstone.platform.source.remote.LevelRemoteDataSource
import com.illidant.easykanzicapstone.platform.source.remote.TestRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.home.HomeActivity
import com.illidant.easykanzicapstone.ui.screen.home.HomeContract
import com.illidant.easykanzicapstone.ui.screen.home.HomePresenter
import com.illidant.easykanzicapstone.ui.screen.learn.LearnActivity
import com.illidant.easykanzicapstone.ui.screen.lesson.LessonContract
import com.illidant.easykanzicapstone.ui.screen.profile.ProfileActivity
import com.illidant.easykanzicapstone.ui.screen.search.SearchActivity
import kotlinx.android.synthetic.main.activity_level.*
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.bottom_navigation_bar.*

class RankingActivity : AppCompatActivity(), RankingContract.View, HomeContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        configView()
        homePresenter.getLevelData()
    }
    private val rankingPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = TestRemoteDataSource(retrofit)
        val repository = TestRepository(remote)
        RankingPresenter(this, repository)
    }
    private val homePresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = LevelRemoteDataSource(retrofit)
        val repository = LevelRepository(remote)
        HomePresenter(this, repository)
    }

    override fun onRankingData(listRank: List<TestRanking>) {
        recyclerRanking!!.layoutManager = GridLayoutManager(this, 1)
        recyclerRanking!!.adapter = RankingAdapter(listRank,this)
    }

    override fun onDataComplete(levels: List<Level>) {
        var level_names = mutableListOf<String>()
        var level_ids = mutableListOf<Int>()
        for(level in levels) {
            level_names.add(level.name)
            level_ids.add(level.id)
        }

        spinnerRanking.adapter = ArrayAdapter<String>(this,R.layout.item_lesson_spinner, level_names)

        spinnerRanking.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(applicationContext,"Please choose a lesson", Toast.LENGTH_SHORT).show()
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var level_id = level_ids.get(p2)
                rankingPresenter.getRankingByLevelIDRequest(level_id)
            }

        }
    }


    private fun configView() {
        //set bottom navigation bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        //set home selected
        bottomNavigationView.selectedItemId =
            R.id.ranking

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
                R.id.ranking ->
                {
                    return@OnNavigationItemSelectedListener true
                    finish()
                }
                R.id.profile -> {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true

                }
            }
            false
        })
    }
}