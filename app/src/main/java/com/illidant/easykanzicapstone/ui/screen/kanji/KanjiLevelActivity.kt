package com.illidant.easykanzicapstone.ui.screen.kanji

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.KanjiRepository
import com.illidant.easykanzicapstone.platform.source.local.SharedPrefs
import com.illidant.easykanzicapstone.platform.source.local.UserLocalDataSource
import com.illidant.easykanzicapstone.platform.source.remote.KanjiRemoteDataSource
import kotlinx.android.synthetic.main.activity_level.*

class KanjiLevelActivity : AppCompatActivity(), KanjiContract.View {
    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val local = UserLocalDataSource.getInstance(SharedPrefs(this))
        val remote = KanjiRemoteDataSource(retrofit)
        val repository = KanjiRepository(remote)
        KanjiPresenter(this, repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)
        initialize()

    }

    companion object {
        fun getIntent(context: Context) = Intent(context, KanjiLevelActivity::class.java)
    }

    private fun initialize() {
        recycler_level.layoutManager = GridLayoutManager(this, 3)
        presenter.kanjiRequest(1)
    }

    override fun fillKanji(listKanjiLesson: List<Kanji>) {
        recycler_level.adapter = KanjiLevelAdapter(this, listKanjiLesson)
    }
}