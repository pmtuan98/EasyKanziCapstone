package com.illidant.easykanzicapstone.ui.screen.learn.multiple_choice

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Quiz
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.QuizRepository
import com.illidant.easykanzicapstone.platform.source.remote.QuizRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.flashcard.FlashcardActivity
import com.illidant.easykanzicapstone.ui.screen.learn.writing.WritingActivity
import com.illidant.easykanzicapstone.ui.screen.quiz.QuizContract
import com.illidant.easykanzicapstone.ui.screen.quiz.QuizPresenter
import kotlinx.android.synthetic.main.activity_multiple_choice.*
import kotlinx.android.synthetic.main.activity_multiple_choice.tv_totalQuestion

class MultipleChoiceActivity : AppCompatActivity(), QuizContract.View {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple_choice)
        initialize()
    }

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = QuizRemoteDataSource(retrofit)
        val repository = QuizRepository(remote)
        QuizPresenter(this, repository)
    }

    private fun initialize() {
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        presenter.quizByLessonRequest(lesson_id)
        buttonExit.setOnClickListener {
            finish()
        }
    }

    private fun showCompleteDialog () {
        val dialog = Dialog(this)
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_complete_multiplechoice)
        val buttonAgain= dialog.findViewById(R.id.buttonLearnAgain) as Button
        val buttonLearnWriting = dialog.findViewById(R.id.buttonLearnWriting) as Button
        val buttonLearnFlashcard = dialog.findViewById(R.id.buttonLearnFlashcard) as Button
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
        buttonLearnFlashcard.setOnClickListener{
            val intent = Intent(it.context, FlashcardActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
    }

    override fun getQuizByLessonID(listQuiz: List<Quiz>) {
        var currentPosition = 0
        var wrongAnswerBackground: Drawable?
        wrongAnswerBackground = ContextCompat.getDrawable(this,R.drawable.bg_wrong_answer)

        //set progress bar
        progressBarMultiple.max = listQuiz.size
        progressBarMultiple.progress = currentPosition

        //Display total question
        tv_totalQuestion.text = listQuiz.size.toString()
        tv_questionNo.text = (currentPosition + 1).toString()

        // Display question
        textQuestion.text = listQuiz[currentPosition].question

        // Display answer
        textAnswerA.text = listQuiz[currentPosition].answerA
        textAnswerB.text = listQuiz[currentPosition].answerB
        textAnswerC.text = listQuiz[currentPosition].answerC
        textAnswerD.text = listQuiz[currentPosition].answerD
        var correctAnswer = listQuiz[currentPosition].correctAnswer
        resetAnswersBackground()
        hideNextButton()

        textAnswerA?.setOnClickListener {
            checkCorrectAnswer(correctAnswer)
            if(!textAnswerA.text.equals(correctAnswer)){
                textAnswerA?.background = wrongAnswerBackground
            }
            displayNextButton()
        }
        textAnswerB?.setOnClickListener {
            checkCorrectAnswer(correctAnswer)
            if(!textAnswerB.text.equals(correctAnswer)){
                textAnswerB?.background = wrongAnswerBackground
            }
            displayNextButton()
        }
        textAnswerC?.setOnClickListener {
            checkCorrectAnswer(correctAnswer)
            if(!textAnswerC.text.equals(correctAnswer)){
                textAnswerC?.background = wrongAnswerBackground
            }
            displayNextButton()
        }
        textAnswerD?.setOnClickListener {
            checkCorrectAnswer(correctAnswer)
            if(!textAnswerD.text.equals(correctAnswer)){
                textAnswerD?.background = wrongAnswerBackground
            }
            displayNextButton()
        }

        btnNextQuestion.setOnClickListener({
            currentPosition++
            if (currentPosition == listQuiz.size) {
                currentPosition = listQuiz.size - 1
                showCompleteDialog()
            }
            tv_questionNo.text = (currentPosition + 1).toString()
            progressBarMultiple.progress = currentPosition + 1
            textQuestion.text = listQuiz[currentPosition].question
            textAnswerA.text = listQuiz[currentPosition].answerA
            textAnswerB.text = listQuiz[currentPosition].answerB
            textAnswerC.text = listQuiz[currentPosition].answerC
            textAnswerD.text = listQuiz[currentPosition].answerD
            correctAnswer = listQuiz[currentPosition].correctAnswer
            resetAnswersBackground()
            hideNextButton()
        })

    }

    override fun getQuizByLevelID(listQuiz: List<Quiz>) {
       //not use
    }

    fun checkCorrectAnswer(answer : String) {
        var correctAnswerBackground: Drawable? = null
        correctAnswerBackground = ContextCompat.getDrawable(this,R.drawable.bg_correct_answer)

        if(textAnswerA.text.equals(answer)){
            textAnswerA?.background = correctAnswerBackground
        }else if (textAnswerB.text.equals(answer)){
            textAnswerB?.background = correctAnswerBackground
        }else if (textAnswerC.text.equals(answer)){
            textAnswerC?.background = correctAnswerBackground
        }else if (textAnswerD.text.equals(answer)){
            textAnswerD?.background = correctAnswerBackground
        }
    }

    fun resetAnswersBackground() {
        val defaultAnswerBackground = ContextCompat.getDrawable(this, R.drawable.bg_detail_kanji)
        textAnswerA?.background = defaultAnswerBackground
        textAnswerB?.background = defaultAnswerBackground
        textAnswerC?.background = defaultAnswerBackground
        textAnswerD?.background = defaultAnswerBackground

        textAnswerA.isEnabled = true
        textAnswerB.isEnabled = true
        textAnswerC.isEnabled = true
        textAnswerD.isEnabled = true
    }

    fun hideNextButton() {
        btnNextQuestion?.visibility = View.INVISIBLE
    }

    fun displayNextButton() {
        btnNextQuestion?.visibility = View.VISIBLE
        textAnswerA.isEnabled = false
        textAnswerB.isEnabled = false
        textAnswerC.isEnabled = false
        textAnswerD.isEnabled = false
    }

}