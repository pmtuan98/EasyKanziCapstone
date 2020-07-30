package com.illidant.easykanzicapstone.ui.screen.ranking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.ui.screen.home.HomeActivity
import com.illidant.easykanzicapstone.ui.screen.profile.ProfileActivity
import com.illidant.easykanzicapstone.ui.screen.search.SearchActivity

class RankingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

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