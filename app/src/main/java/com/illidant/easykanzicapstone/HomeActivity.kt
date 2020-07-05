package com.illidant.easykanzicapstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    var imageList: ArrayList<Int> = ArrayList()
    var titleList: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initialize()

        //set home selected
        bottom_navigation.selectedItemId = R.id.home

        //Perform ItemSelectedListener
        bottom_navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> return@OnNavigationItemSelectedListener true
                R.id.search -> {
                    startActivity(Intent(applicationContext, SearchActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.ranking -> {
                    startActivity(Intent(applicationContext, RankingActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
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
        imageList.add(R.drawable.jpd111)
        imageList.add(R.drawable.jpd121)
        imageList.add(R.drawable.jpd131)
        imageList.add(R.drawable.jpd141)
        titleList.add("JPD111")
        titleList.add("JPD121")
        titleList.add("JPD131")
        titleList.add("JPD141")
        recycler_home!!.adapter = HomePageAdapter(imageList, titleList)
    }
}