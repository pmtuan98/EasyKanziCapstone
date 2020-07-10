package com.illidant.easykanzicapstone.ui.screen.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.illidant.easykanzicapstone.*
import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.LevelRepository
import com.illidant.easykanzicapstone.platform.source.local.SharedPrefs
import com.illidant.easykanzicapstone.platform.source.local.UserLocalDataSource
import com.illidant.easykanzicapstone.platform.source.remote.LevelRemoteDataSource

import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(),HomeContract.View {
    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val local = UserLocalDataSource.getInstance(SharedPrefs(this))
        val remote = LevelRemoteDataSource(retrofit)
        val repository = LevelRepository(remote)
        HomePresenter(this, repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initialize()

        //set home selected
        bottom_navigation.selectedItemId =
            R.id.home

        //Perform ItemSelectedListener
        bottom_navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home ->
                {
                    return@OnNavigationItemSelectedListener true
                    finish()
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
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true

                }
            }
            false
        })
    }

    private fun initialize() {
        val gridLayoutManager = GridLayoutManager(this, 2)
        recycler_home!!.layoutManager = gridLayoutManager
        presenter.getLevelData()
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    override fun onDataComplete(levels: List<Level>) {
        recycler_home!!.adapter = HomePageAdapter(levels)

    }
}
