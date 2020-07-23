package com.illidant.easykanzicapstone.ui.screen.learn.multiple_choice

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Quiz
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.QuizRepository
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import com.illidant.easykanzicapstone.platform.source.remote.QuizRemoteDataSource
import com.illidant.easykanzicapstone.platform.source.remote.VocabularyRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.LearnPresenter
import com.illidant.easykanzicapstone.ui.screen.quiz.QuizContract
import com.illidant.easykanzicapstone.ui.screen.quiz.QuizPresenter
import kotlinx.android.synthetic.main.activity_multiple_choice.*

class MultipleChoiceActivity : AppCompatActivity(), QuizContract.View {
    var currentPosition = 0
    var totalQuestions = 0

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
    }

    override fun getQuizByLessonID(listQuiz: List<Quiz>) {
        var wrongAnswerBackground: Drawable? = null
        wrongAnswerBackground = ContextCompat.getDrawable(this,R.drawable.bg_wrong_answer)

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