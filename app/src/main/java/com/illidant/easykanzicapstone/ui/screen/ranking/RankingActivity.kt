package com.illidant.easykanzicapstone.ui.screen.ranking

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.illidant.easykanzicapstone.BaseActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.domain.model.TestRanking
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.LevelRepository
import com.illidant.easykanzicapstone.platform.repository.TestRepository
import com.illidant.easykanzicapstone.platform.source.remote.LevelRemoteDataSource
import com.illidant.easykanzicapstone.platform.source.remote.TestRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.home.HomeActivity
import com.illidant.easykanzicapstone.ui.screen.home.HomeContract
import com.illidant.easykanzicapstone.ui.screen.home.HomePresenter
import com.illidant.easykanzicapstone.ui.screen.profile.ProfileActivity
import com.illidant.easykanzicapstone.ui.screen.search.SearchActivity
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.activity_ranking.btnJPD111
import kotlinx.android.synthetic.main.activity_ranking.btnJPD121
import kotlinx.android.synthetic.main.activity_ranking.btnJPD131
import kotlinx.android.synthetic.main.activity_test_history.*

class RankingActivity : BaseActivity(), RankingContract.View {

    private var listRanking: MutableList<TestRanking> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        setDisplayDefault()
        configView()
    }

    private val rankingPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = TestRemoteDataSource(retrofit)
        val repository = TestRepository(remote)
        RankingPresenter(this, repository)
    }
    private fun changeButtonColor(){
        if(btnJPD111.isPressed){
            btnJPD111.background =  ContextCompat.getDrawable(this, R.drawable.btn_ranking_pressed)
            btnJPD111.setTextColor(Color.BLACK)
            btnJPD121.background =  ContextCompat.getDrawable(this, R.drawable.btn_ranking_default)
            btnJPD121.setTextColor(Color.GRAY)
            btnJPD131.background =  ContextCompat.getDrawable(this, R.drawable.btn_ranking_default)
            btnJPD131.setTextColor(Color.GRAY)
        }else if(btnJPD121.isPressed) {
            btnJPD111.background =  ContextCompat.getDrawable(this, R.drawable.btn_ranking_default)
            btnJPD111.setTextColor(Color.GRAY)
            btnJPD121.background =  ContextCompat.getDrawable(this, R.drawable.btn_ranking_pressed)
            btnJPD121.setTextColor(Color.BLACK)
            btnJPD131.background =  ContextCompat.getDrawable(this, R.drawable.btn_ranking_default)
            btnJPD131.setTextColor(Color.GRAY)
        }else if(btnJPD131.isPressed) {
            btnJPD111.background =  ContextCompat.getDrawable(this, R.drawable.btn_ranking_default)
            btnJPD111.setTextColor(Color.GRAY)
            btnJPD121.background =  ContextCompat.getDrawable(this, R.drawable.btn_ranking_default)
            btnJPD121.setTextColor(Color.GRAY)
            btnJPD131.background =  ContextCompat.getDrawable(this, R.drawable.btn_ranking_pressed)
            btnJPD131.setTextColor(Color.BLACK)
        }
    }
    private fun setDisplayDefault() {
        btnJPD111.background =  ContextCompat.getDrawable(this, R.drawable.btn_ranking_pressed)
        btnJPD111.setTextColor(Color.BLACK)
        rankingPresenter.getRankingByLevelIDRequest(1)
    }

    private fun changeData() {
            btnJPD111.setOnClickListener {
                changeButtonColor()
                rankingPresenter.getRankingByLevelIDRequest(1)
            }
            btnJPD121.setOnClickListener {
                changeButtonColor()
                rankingPresenter.getRankingByLevelIDRequest(2)
            }
            btnJPD131.setOnClickListener {
                changeButtonColor()
                rankingPresenter.getRankingByLevelIDRequest(3)
            }
    }
    private fun displayTopThree() {
        tvNameTop1.text = listRanking[0].userName
        tvNameTop2.text = listRanking[1].userName
        tvNameTop3.text = listRanking[2].userName
        tvPointTop1.text = "${listRanking[0].resultPoint}pts"
        tvPointTop2.text = "${listRanking[1].resultPoint}pts"
        tvPointTop3.text = "${listRanking[2].resultPoint}pts"
        testTime1 = listRanking[0].timeTaken.toInt()
        testTime2 = listRanking[1].timeTaken.toInt()
        testTime3 = listRanking[2].timeTaken.toInt()
        formatTime(testTime1)
        tvTimeTop1.text = "${takenMinutes}m ${takenSeconds}s"
        formatTime(testTime2)
        tvTimeTop2.text = "${takenMinutes}m ${takenSeconds}s"
        formatTime(testTime3)
        tvTimeTop3.text = "${takenMinutes}m ${takenSeconds}s"

    }
    private var testTime1 = 0
    private var testTime2 = 0
    private var testTime3 = 0
    private var takenMinutes = ""
    private var takenSeconds = ""
    private fun formatTime(testTime: Int) {
        takenMinutes = (testTime / 60).toString()
        takenSeconds = (testTime % 60).toString()
    }

    override fun onRankingData(listRank: List<TestRanking>) {
        listRanking.clear()
        changeData()
        listRanking.addAll(listRank)
        displayTopThree()
        recyclerViewRanking!!.layoutManager = GridLayoutManager(this, 1)
        recyclerViewRanking!!.adapter = RankingAdapter(listRank, this)
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun configView() {
        //set home selected
        bottomNavBar.selectedItemId = R.id.ranking

        //Perform ItemSelectedListener
        bottomNavBar.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
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
}