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
import kotlinx.android.synthetic.main.flashcard_layout_back.*
import kotlinx.android.synthetic.main.flashcard_layout_front.*


class FlashcardActivity : AppCompatActivity(), LearnContract.View {
    private var x1 = 0f
    private var x2 = 0f
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
    private fun initialize() {
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        presenter.vocabByLessonRequest(lesson_id)


        buttonFinish.setOnClickListener {
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

        //Set progress bar
        progressBarFlashcard.max = kanjiList.size
        progressBarFlashcard.progress = counter

        // Next button click
        btn_flashcard_next.setOnClickListener {
           if(flash_card.isBackSide) {
               flash_card.flipTheView(true)
               layout_back.visibility = View.GONE
               next()
           }else next()
        }

        //Previous button click
        btn_flashcard_previous.setOnClickListener {
            if(flash_card.isBackSide) {
                flash_card.flipTheView()
                layout_back.visibility = View.GONE
                previous()
            }else  previous()

        }
    }
    private fun showCompleteDialog () {
        val dialog = Dialog(this)
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_complete_flashcard)
        val buttonAgain= dialog.findViewById(R.id.buttonLearnAgain) as Button
        val buttonLearnWriting = dialog.findViewById(R.id.buttonLearnWriting) as Button
        val buttonLearnMultiple = dialog.findViewById(R.id.buttonMultipleChoice) as Button
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

    fun previous(){
        counter--
        if (counter < 0) {
            counter = 0
        }
        flashcard_kanji.text = kanjiList[counter]
        flashcard_meaning.text = meaningList[counter]
        flashcard_hira.text = hiraganaList[counter]
        tv_questionNo.text = (counter + 1).toString()
        progressBarFlashcard.progress = counter

    }
    fun next() {
        counter++
        if (counter == kanjiList.size) {
            counter = kanjiList.size - 1
            showCompleteDialog()
        }
        flashcard_kanji.text = kanjiList[counter]
        flashcard_meaning.text = meaningList[counter]
        flashcard_hira.text = hiraganaList[counter]
        tv_questionNo.text = (counter + 1).toString()
        progressBarFlashcard.progress = counter+1

    }




}