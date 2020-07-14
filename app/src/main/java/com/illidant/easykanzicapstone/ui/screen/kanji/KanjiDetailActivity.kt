package com.illidant.easykanzicapstone.ui.screen.kanji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.KanjiRepository
import com.illidant.easykanzicapstone.platform.source.remote.KanjiRemoteDataSource

class KanjiDetailActivity : AppCompatActivity(), KanjiContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanji_detail)
    }

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = KanjiRemoteDataSource(retrofit)
        val repository = KanjiRepository(remote)
        KanjiPresenter(this, repository)
    }

    override fun getKanjiByLesson(listKanjiLesson: List<Kanji>) {
       
    }
}