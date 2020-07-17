package com.illidant.easykanzicapstone.ui.screen.learn.flashcard

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import com.illidant.easykanzicapstone.platform.source.remote.VocabularyRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.LearnContract
import com.illidant.easykanzicapstone.ui.screen.learn.LearnPresenter
import kotlinx.android.synthetic.main.activity_flashcard.*
import kotlinx.android.synthetic.main.flashcard_layout_back.*
import kotlinx.android.synthetic.main.flashcard_layout_front.*


class FlashcardActivity : AppCompatActivity(), LearnContract.View {
    private var x1 = 0f
    private  var x2:kotlin.Float = 0f
    val MIN_DISTANCE = 150
    var counter = 0
    val kanjiList : MutableList<String> = mutableListOf()
    val hiraganaList : MutableList<String> = mutableListOf()
    val meaningList : MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)
        initialize()
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
                                Toast.makeText(v!!.context, "Left to Right swipe [Next]", Toast.LENGTH_SHORT)
                                    .show()
                                previous()
                            } else {
                                Toast.makeText(v!!.context, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT)
                                    .show()
                                next()
                            }
                        } else {
                            // consider as something else - a screen tap for example
                        }
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })


    }
    private fun initialize() {
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        val lesson_name = intent.getStringExtra("LESSON_NAME")
        val level_name = intent.getStringExtra("LEVEL_NAME")
        txt_level.text = level_name
        txt_lesson.text = lesson_name
        presenter.vocabByLessonRequest(lesson_id)
    }

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = VocabularyRemoteDataSource(retrofit)
        val repository = VocabularyRepository(remote)
        LearnPresenter(this, repository)
    }

    override fun getVocabByKanjiID(listVocab: List<Vocabulary>) {
       // Not use
    }

    override fun getVocabByLessonID(listVocab: List<Vocabulary>) {

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

        // Next button click
        btn_flashcard_next.setOnClickListener {
            next()
        }

        //Previous button click
        btn_flashcard_previous.setOnClickListener {
            previous()
        }
    }
    fun previous(){
        counter--
        if (counter < 0) {
            counter = kanjiList.size - 1
        }
        flashcard_kanji.text = kanjiList[counter]
        flashcard_meaning.text = meaningList[counter]
        flashcard_hira.text = hiraganaList[counter]
        tv_questionNo.text = (counter + 1).toString()
    }
    fun next() {
        counter++
        if (counter == kanjiList.size) {
            counter = 0
        }
        flashcard_kanji.text = kanjiList[counter]
        flashcard_meaning.text = meaningList[counter]
        flashcard_hira.text = hiraganaList[counter]
        tv_questionNo.text = (counter + 1).toString()
    }

}