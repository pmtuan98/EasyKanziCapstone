package com.illidant.easykanzicapstone.ui.screen.learn.flashcard

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.MotionEvent
import android.view.View
import com.illidant.easykanzicapstone.BaseActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import com.illidant.easykanzicapstone.platform.source.remote.VocabularyRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.LearnContract
import com.illidant.easykanzicapstone.ui.screen.learn.LearnPresenter
import kotlinx.android.synthetic.main.activity_kanji_flashcard.*
import kotlinx.android.synthetic.main.flashcard_layout_back.*
import kotlinx.android.synthetic.main.flashcard_layout_front.*
import java.util.*

class KanjiFlashcardActivity : BaseActivity(), LearnContract.View {
    private var x1 = 0f
    private var x2 = 0f
    val MIN_DISTANCE = 150
    var counter = 0
    val vocabularyList: MutableList<Vocabulary> = mutableListOf()
    lateinit var mTTS: TextToSpeech
    private lateinit var textHiragana: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanji_flashcard)
        initialize()
    }

    private fun initialize() {
        //Learn flashcard by kanji
        val kanjiId = intent.getIntExtra("KANJI_ID", 0)
        presenter.vocabByKanjiIDRequest(kanjiId)

        swipeFlashcard()
        configViews()
        setUpSpeaker()
    }

    private fun configViews() {
        btnExit.setOnClickListener {
            finish()
        }
        btnSpeak.setOnClickListener {
            speak()
        }
    }

    private fun speak() {
        mTTS.speak(textHiragana, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun setUpSpeaker() {
        mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                //if there is no error then set language
                mTTS.language = Locale.JAPANESE
            }
        })
        // set pitch
        mTTS.setPitch(1f)
        // set speed of speak
        mTTS.setSpeechRate(1f)
    }

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = VocabularyRemoteDataSource(retrofit)
        val repository = VocabularyRepository(remote)
        LearnPresenter(this, repository)
    }

    private fun swipeFlashcard() {
        flashCard.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> x1 = event.x
                    MotionEvent.ACTION_UP -> {
                        x2 = event.x
                        val deltaX = x2 - x1
                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            // Left to Right swipe action
                            if (x2 > x1) {
                                if (flashCard.isBackSide) {
                                    flashCard.flipTheView()
                                    layoutBack.visibility = View.GONE
                                    previous()
                                } else previous()
                            } else {
                                if (flashCard.isBackSide) {
                                    flashCard.flipTheView(true)
                                    layoutBack.visibility = View.GONE
                                    next()
                                } else next()
                            }
                        }
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
    }

    fun checkNextBackButton(counter: Int) {
        if (counter == 0) {
            btnBack.isEnabled = false
        } else if (counter == vocabularyList.size - 1) {
            btnNext.isEnabled = false
        } else {
            btnBack.isEnabled = true
            btnNext.isEnabled = true
        }
    }

    override fun getVocabByKanjiID(listVocab: List<Vocabulary>) {
        vocabularyList.addAll(listVocab)

        //First appearance
        flashcardKanji.text = vocabularyList[counter].kanji_vocab
        flashcardHira.text = vocabularyList[counter].hiragana
        flashcardVietnamese.text = vocabularyList[counter].vocab_meaning
        //Setup for speaker
        textHiragana = vocabularyList[counter].hiragana
        //Display total question
        tvTotalQuestion.text = vocabularyList.size.toString()
        tvQuestionNo.text = (counter + 1).toString()

        //Set progress bar
        progressBarFlashcard.max = vocabularyList.size
        progressBarFlashcard.progress = counter + 1
        checkNextBackButton(counter)

        // Next button click
        btnNext.setOnClickListener {
            if (flashCard.isBackSide) {
                flashCard.flipTheView(true)
                layoutBack.visibility = View.GONE
                next()
            } else next()
        }

        //Previous button click
        btnBack.setOnClickListener {
            if (flashCard.isBackSide) {
                flashCard.flipTheView()
                layoutBack.visibility = View.GONE
                previous()
            } else previous()

        }
    }

    private fun previous() {
        counter--
        if (counter < 0) {
            counter = 0
        }
        checkNextBackButton(counter)
        flashcardKanji.text = vocabularyList[counter].kanji_vocab
        flashcardVietnamese.text = vocabularyList[counter].vocab_meaning
        flashcardHira.text = vocabularyList[counter].hiragana
        tvQuestionNo.text = (counter + 1).toString()
        progressBarFlashcard.progress = counter + 1
        //Setup for speaker
        textHiragana = vocabularyList[counter].hiragana

    }

    private fun next() {
        counter++
        if (counter == vocabularyList.size) {
            counter = vocabularyList.size - 1
        }
        checkNextBackButton(counter)
        flashcardKanji.text = vocabularyList[counter].kanji_vocab
        flashcardVietnamese.text = vocabularyList[counter].vocab_meaning
        flashcardHira.text = vocabularyList[counter].hiragana
        tvQuestionNo.text = (counter + 1).toString()
        progressBarFlashcard.progress = counter + 1
        //Setup for speaker
        textHiragana = vocabularyList[counter].hiragana
    }

    override fun getVocabByLessonID(listVocab: List<Vocabulary>) {
        //Not use
    }
}