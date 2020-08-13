package com.illidant.easykanzicapstone.ui.screen.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.ui.screen.ranking.RankingActivity
import com.illidant.easykanzicapstone.domain.model.KanjiES
import com.illidant.easykanzicapstone.domain.request.SearchRequest
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.SearchRepository
import com.illidant.easykanzicapstone.platform.source.remote.SearchRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.home.HomeActivity
import com.illidant.easykanzicapstone.ui.screen.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), SearchContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        configViews()
        searchKanji()
    }

    private val searchPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = SearchRemoteDataSource(retrofit)
        val repository = SearchRepository(remote)
        SearchPresenter(this, repository)
    }

    private fun searchKanji() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                val searchRequest = SearchRequest("$newText")
                searchPresenter.searchKanji(searchRequest)

                if (newText.trim().isEmpty()) {
                    recyclerViewSearch.visibility = View.INVISIBLE
                    //Not found
                    notFoundImage.visibility = View.INVISIBLE
                    tvNotFound.visibility = View.INVISIBLE
                    //Found
                    searchImage.visibility = View.VISIBLE
                    tvSearching.visibility = View.VISIBLE

                } else {
                    recyclerViewSearch.visibility = View.VISIBLE

                    searchImage.visibility = View.INVISIBLE
                    tvSearching.visibility = View.INVISIBLE
                }

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
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

    private fun configViews() {
        //set home selected
        bottomNavBar.selectedItemId = R.id.search

        //Perform ItemSelectedListener
        bottomNavBar.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.search -> {
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

    override fun onSearchResult(listSearch: List<KanjiES>) {
        recyclerViewSearch!!.layoutManager = GridLayoutManager(this, 1)
        recyclerViewSearch!!.adapter = SearchAdapter(listSearch, this)

        if (listSearch.isEmpty()) {
            notFoundImage.visibility = View.VISIBLE
            tvNotFound.visibility = View.VISIBLE
        } else {
            notFoundImage.visibility = View.INVISIBLE
            tvNotFound.visibility = View.INVISIBLE
        }

    }
}