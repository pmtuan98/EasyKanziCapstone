package com.illidant.easykanzicapstone.ui.screen.kanji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.KanjiRepository
import com.illidant.easykanzicapstone.platform.source.remote.KanjiRemoteDataSource
import kotlinx.android.synthetic.main.activity_kanji_detail.*

class KanjiDetailActivity : AppCompatActivity(), KanjiContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanji_detail)
        initialize()
    }

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = KanjiRemoteDataSource(retrofit)
        val repository = KanjiRepository(remote)
        KanjiPresenter(this, repository)
    }

    private fun initialize() {
        var kanji_id = intent.getIntExtra("KANJI_ID",0)
        Log.d("Receive_KANJI_ID", kanji_id.toString())
        presenter.kanjiByIDRequest(kanji_id)

    }

    override fun getKanjiByLesson(listKanjiLesson: List<Kanji>) {
       //Not use
    }

    override fun getKanjiByID(kanjiAttribute: Kanji) {
        textKanjiChinaMean.text = kanjiAttribute.sino_vietnamese
        textKanjiVietMean.text = kanjiAttribute.kanji_meaning
        textKanjiOnyomi.text = kanjiAttribute.onyomi
        textKanjiKunyomi.text = kanjiAttribute.kunyomi
        textOnFurigana.text = kanjiAttribute.on_furigana
        textKunFurigana.text = kanjiAttribute.kun_furigana
    }
}