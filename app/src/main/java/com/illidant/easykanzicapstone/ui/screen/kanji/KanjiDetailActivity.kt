package com.illidant.easykanzicapstone.ui.screen.kanji

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.KanjiRepository
import com.illidant.easykanzicapstone.platform.source.remote.KanjiRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.flashcard.KanjiFlashcardActivity
import kotlinx.android.synthetic.main.activity_kanji_detail.*
import java.util.*

class KanjiDetailActivity : AppCompatActivity(), KanjiContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanji_detail)
        initialize()
        configViews()
    }

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = KanjiRemoteDataSource(retrofit)
        val repository = KanjiRepository(remote)
        KanjiPresenter(this, repository)
    }

    private fun configViews() {
        btnBack.setOnClickListener {
            finish()
        }
        btnFlashcard.setOnClickListener {
            var kanji_id = intent.getIntExtra("KANJI_ID",0)
            val intent = Intent(it.context, KanjiFlashcardActivity::class.java)
            intent.putExtra("KANJI_ID", kanji_id)
            startActivity(intent)
        }
    }

    private fun initialize() {
        var kanjiId = intent.getIntExtra("KANJI_ID", 0)
        presenter.kanjiByIDRequest(kanjiId)
    }


    override fun getKanjiByLesson(listKanjiLesson: List<Kanji>) {
        //Not use
    }

    override fun getKanjiByID(kanjiAttribute: Kanji) {
        var stringStroke: String
        tvChineseMean.text = kanjiAttribute.sino_vietnamese
        tvVietnameseMean.text = kanjiAttribute.kanji_meaning
        tvKanjiOnyomi.text = kanjiAttribute.onyomi
        tvKanjiKunyomi.text = kanjiAttribute.kunyomi
        tvOnFurigana.text = kanjiAttribute.on_furigana
        tvKunFurigana.text = kanjiAttribute.kun_furigana
        stringStroke = kanjiAttribute.image
        handleKanjiStroke(stringStroke)
    }

    private fun handleKanjiStroke(input: String) {
        var listStroke = mutableListOf<String>()
        val delimiter = "|"
        val parts = input.split(delimiter)
        for (i in 0..parts.size.minus(1)) {
            listStroke.add(parts[i])
        }
        kanjiStroke.loadPathData(listStroke)
        kanjiStroke.autoRun = true
        kanjiStroke.setOnClickListener({
            kanjiStroke.startDrawAnimation()
        })
    }

    override fun getVocabByKanjiID(listVocab: List<Vocabulary>) {
        recyclerViewVocab.layoutManager = GridLayoutManager(this, 1)
        recyclerViewVocab.adapter = KanjiDetailAdapter(listVocab, this)
    }
}