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
    lateinit var mTTS: TextToSpeech
    private lateinit var kanji:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanji_detail)
        initialize()
        speak.setOnClickListener{
            speak()
        }
    }

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = KanjiRemoteDataSource(retrofit)
        val repository = KanjiRepository(remote)
        KanjiPresenter(this, repository)
    }

    private fun initialize() {
        var kanji_id = intent.getIntExtra("KANJI_ID",0)
        presenter.kanjiByIDRequest(kanji_id)
        learnVocabByKanji()
        mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR){
                //if there is no error then set language
                mTTS.language = Locale.JAPANESE
            }
        })
        // set pitch
        mTTS.setPitch(0.75f)
        // set speed of speak
        mTTS.setSpeechRate(0.75f)

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
        kanji = kanjiAttribute.onyomi
        stringStroke = kanjiAttribute.image
        handleKanjiStroke(stringStroke)
    }
    fun learnVocabByKanji() {
        btnFlashcard.setOnClickListener {
            var kanji_id = intent.getIntExtra("KANJI_ID",0)
            val intent = Intent(it.context, KanjiFlashcardActivity::class.java)
            intent.putExtra("KANJI_ID", kanji_id)
            startActivity(intent)
        }
    }

    fun handleKanjiStroke(input: String) {
        var listStroke = mutableListOf<String>()
        val delimiter ="|"
        val parts = input.split(delimiter)
        for (i in 0 .. parts.size.minus(1)) {
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
    private fun speak(){
        mTTS.speak(kanji, TextToSpeech.QUEUE_FLUSH, null)
    }

}