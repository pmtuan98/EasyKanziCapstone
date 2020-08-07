package com.illidant.easykanzicapstone.ui.screen.learn.flashcard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
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

class KanjiFlashcardActivity : AppCompatActivity(), LearnContract.View {
    private var x1 = 0f
    private var x2 = 0f
    val MIN_DISTANCE = 150
    var counter = 0
    val kanjiList : MutableList<String> = mutableListOf()
    val hiraganaList : MutableList<String> = mutableListOf()
    val meaningList : MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanji_flashcard)
        initialize()
    }
    private fun initialize() {
        //Learn flashcard by kanji
        val kanji_id = intent.getIntExtra("KANJI_ID", 0)
        presenter.vocabByKanjiIDRequest(kanji_id)

        swipeFlashcard()

        buttonRestart.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
        }
        buttonExit.setOnClickListener {
            finish()
        }

    }
    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = VocabularyRemoteDataSource(retrofit)
        val repository = VocabularyRepository(remote)
        LearnPresenter(this, repository)
    }

    private fun swipeFlashcard() {
        flash_card.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> x1 = event.x
                    MotionEvent.ACTION_UP -> {
                        x2 = event.x
                        val deltaX = x2 - x1
                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            // Left to Right swipe action
                            if (x2 > x1) {
                                if(flash_card.isBackSide) {
                                    flash_card.flipTheView()
                                    layout_back.visibility = View.GONE
                                    previous()
                                }else  previous()
                            } else {
                                if(flash_card.isBackSide) {
                                    flash_card.flipTheView(true)
                                    layout_back.visibility = View.GONE
                                    next()
                                }else next()
                            }
                        }
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
    }
    fun checkNextBackButton(counter : Int) {
        if(counter == 0) {
            btn_flashcard_back.isEnabled = false
        }else if (counter == kanjiList.size - 1){
            btn_flashcard_next.isEnabled = false
        }else {
            btn_flashcard_back.isEnabled = true
            btn_flashcard_next.isEnabled = true
        }
    }

    override fun getVocabByKanjiID(listVocab: List<Vocabulary>) {
        // Add to list
        for (vocab in listVocab) {
            kanjiList.add(vocab.kanji_vocab)
            hiraganaList.add(vocab.hiragana)
            meaningList.add(vocab.vocab_meaning)
        }
        //First appearance
        flashcard_kanji.text = kanjiList[counter]
        flashcard_hira.text = hiraganaList[counter]
        flashcard_meaning.text = meaningList[counter]

        //Display total question
        tv_totalQuestion.text = kanjiList.size.toString()
        tv_questionNo.text = (counter + 1).toString()

        //Set progress bar
        progressBarFlashcard.max = kanjiList.size
        progressBarFlashcard.progress = counter+1
        checkNextBackButton(counter)

        // Next button click
        btn_flashcard_next.setOnClickListener {
            if(flash_card.isBackSide) {
                flash_card.flipTheView(true)
                layout_back.visibility = View.GONE
                next()
            }else next()
        }

        //Previous button click
        btn_flashcard_back.setOnClickListener {
            if(flash_card.isBackSide) {
                flash_card.flipTheView()
                layout_back.visibility = View.GONE
                previous()
            }else  previous()

        }
    }
    private fun previous(){
        counter--
        if (counter < 0) {
            counter = 0
        }
        checkNextBackButton(counter)
        flashcard_kanji.text = kanjiList[counter]
        flashcard_meaning.text = meaningList[counter]
        flashcard_hira.text = hiraganaList[counter]
        tv_questionNo.text = (counter + 1).toString()
        progressBarFlashcard.progress = counter+1

    }
    private fun next() {
        counter++
        if (counter == kanjiList.size) {
            counter = kanjiList.size - 1
        }
        checkNextBackButton(counter)
        flashcard_kanji.text = kanjiList[counter]
        flashcard_meaning.text = meaningList[counter]
        flashcard_hira.text = hiraganaList[counter]
        tv_questionNo.text = (counter + 1).toString()
        progressBarFlashcard.progress = counter+1

    }

    override fun getVocabByLessonID(listVocab: List<Vocabulary>) {
       //Not use
    }
}