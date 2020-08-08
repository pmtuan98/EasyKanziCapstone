package com.illidant.easykanzicapstone.ui.screen.learn.flashcard

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import com.illidant.easykanzicapstone.platform.source.remote.VocabularyRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.LearnContract
import com.illidant.easykanzicapstone.ui.screen.learn.LearnPresenter
import com.illidant.easykanzicapstone.ui.screen.learn.multiple_choice.MultipleChoiceActivity
import com.illidant.easykanzicapstone.ui.screen.learn.writing.WritingActivity
import kotlinx.android.synthetic.main.activity_flashcard.*
import kotlinx.android.synthetic.main.activity_flashcard.btnExit
import kotlinx.android.synthetic.main.activity_flashcard.flashCard
import kotlinx.android.synthetic.main.activity_flashcard.layoutBack
import kotlinx.android.synthetic.main.activity_flashcard.progressBarFlashcard
import kotlinx.android.synthetic.main.activity_flashcard.tvQuestionNo
import kotlinx.android.synthetic.main.activity_flashcard.tvTotalQuestion
import kotlinx.android.synthetic.main.flashcard_layout_back.*
import kotlinx.android.synthetic.main.flashcard_layout_front.*


class FlashcardActivity : AppCompatActivity(), LearnContract.View {
    private var x1 = 0f
    private var x2 = 0f
    val MIN_DISTANCE = 150
    var counter = 0
    val vocabularyList : MutableList<Vocabulary> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)
        initialize()

    }
    private fun initialize() {

        //Learn flashcard by lesson
        val lessonId = intent.getIntExtra("LESSON_ID", 0)
        presenter.vocabByLessonRequest(lessonId)
        swipeFlashcard()

        btnRestart.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
        }
        btnExit.setOnClickListener {
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
                                if(flashCard.isBackSide) {
                                    flashCard.flipTheView()
                                    layoutBack.visibility = View.GONE
                                    previous()
                                }else  previous()
                            } else {
                                if(flashCard.isBackSide) {
                                    flashCard.flipTheView(true)
                                    layoutBack.visibility = View.GONE
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
            btnBack.isEnabled = false
        }else if (counter == vocabularyList.size){
            btnNext.isEnabled = false
        }else {
            btnBack.isEnabled = true
            btnNext.isEnabled = true
        }
    }
    override fun getVocabByLessonID(listVocab: List<Vocabulary>) {
        vocabularyList.addAll(listVocab)

        //First appearance
        flashcardKanji.text = vocabularyList[counter].kanji_vocab
        flashcardHira.text =  vocabularyList[counter].hiragana
        flashcardVietnamese.text = vocabularyList[counter].vocab_meaning

        //Display total question
        tvTotalQuestion.text = vocabularyList.size.toString()
        tvQuestionNo.text = (counter + 1).toString()

        //Set progress bar
        progressBarFlashcard.max = vocabularyList.size
        progressBarFlashcard.progress = counter+1
        checkNextBackButton(counter)

        // Next button click
        btnNext.setOnClickListener {
           if(flashCard.isBackSide) {
               flashCard.flipTheView(true)
               layoutBack.visibility = View.GONE
               next()
           }else next()
        }

        //Back button click
        btnBack.setOnClickListener {
            if(flashCard.isBackSide) {
                flashCard.flipTheView()
                layoutBack.visibility = View.GONE
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
        flashcardKanji.text = vocabularyList[counter].kanji_vocab
        flashcardVietnamese.text = vocabularyList[counter].vocab_meaning
        flashcardHira.text = vocabularyList[counter].hiragana
        tvQuestionNo.text = (counter + 1).toString()
        progressBarFlashcard.progress = counter+1

    }

    private fun next() {
        counter++
        if (counter == vocabularyList.size) {
            counter = vocabularyList.size - 1
            showCompleteDialog()
        }
         checkNextBackButton(counter)
        flashcardKanji.text = vocabularyList[counter].kanji_vocab
        flashcardVietnamese.text = vocabularyList[counter].vocab_meaning
        flashcardHira.text = vocabularyList[counter].hiragana
        tvQuestionNo.text = (counter + 1).toString()
        progressBarFlashcard.progress = counter+1

    }

    private fun saveRemember() {

    }


    private fun showCompleteDialog () {
        val dialog = Dialog(this)
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_complete_flashcard)
        val buttonAgain= dialog.findViewById(R.id.btnLearnAgain) as Button
        val buttonLearnWriting = dialog.findViewById(R.id.btnLearnWriting) as Button
        val buttonLearnMultiple = dialog.findViewById(R.id.btnMultipleChoice) as Button
        dialog.show()
        buttonAgain.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
            dialog.dismiss()
        }
        buttonLearnWriting.setOnClickListener{
            val intent = Intent(it.context, WritingActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
        buttonLearnMultiple.setOnClickListener{
            val intent = Intent(it.context, MultipleChoiceActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
    }
    override fun getVocabByKanjiID(listVocab: List<Vocabulary>) {
        //Not use
    }
}