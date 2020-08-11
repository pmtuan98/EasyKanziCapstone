package com.illidant.easykanzicapstone.ui.screen.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.illidant.easykanzicapstone.*
import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.LevelRepository
import com.illidant.easykanzicapstone.platform.source.remote.LevelRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.profile.ProfileActivity
import com.illidant.easykanzicapstone.ui.screen.ranking.RankingActivity
import com.illidant.easykanzicapstone.ui.screen.search.SearchActivity

import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(),HomeContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initialize()
        configView()
    }
    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = LevelRemoteDataSource(retrofit)
        val repository = LevelRepository(remote)
        HomePresenter(this, repository)
    }

    private fun initialize() {
        presenter.getLevelData()
        val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        val username = prefs.getString("userName", null)
        tvUsername.text = username
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    override fun onDataComplete(levels: List<Level>) {
        recyclerViewHome!!.layoutManager = GridLayoutManager(this, 1)
        recyclerViewHome!!.adapter = HomeAdapter(this, levels)
        recyclerViewHome.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun configView() {
        //set home selected
        bottomNavBar.selectedItemId = R.id.home

        //Perform ItemSelectedListener
        bottomNavBar.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.search -> {
                    startActivity(Intent(applicationContext, SearchActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    return@OnNavigationItemSelectedListener true

                }
                R.id.ranking -> {
                    startActivity(Intent(applicationContext, RankingActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    return@OnNavigationItemSelectedListener true

                }

                R.id.profile -> {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }
}
