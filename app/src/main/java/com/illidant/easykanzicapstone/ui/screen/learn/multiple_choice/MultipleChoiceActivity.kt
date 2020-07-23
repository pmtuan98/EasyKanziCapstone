package com.illidant.easykanzicapstone.ui.screen.learn.multiple_choice

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    val listQuestion : MutableList<String> = mutableListOf()
    val listAnswerA : MutableList<String> = mutableListOf()
    val listAnswerB : MutableList<String> = mutableListOf()
    val listAnswerC : MutableList<String> = mutableListOf()
    val listAnswerD : MutableList<String> = mutableListOf()
    val listCorrectAnswer: MutableList<String> = mutableListOf()
    var currentQuestion = START_QUESTION
    var pickedAnswer = 0
    var correctedAnswersNumber = 0
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
        for (quiz in listQuiz) {
            listQuestion.add(quiz.question)
            listAnswerA.add(quiz.answerA)
            listAnswerB.add(quiz.answerB)
            listAnswerC.add(quiz.answerC)
            listAnswerD.add(quiz.answerD)
            listCorrectAnswer.add(quiz.correctAnswer)
        }

//        textQuestion.text = listQuestion[currentQuestion]
//        textAnswerA.text = listAnswerA[currentQuestion]
//        textAnswerB.text = listAnswerB[currentQuestion]
//        textAnswerC.text = listAnswerC[currentQuestion]
//        textAnswerD.text = listAnswerD[currentQuestion]
//        val correctAnswer = listCorrectAnswer[currentQuestion]

        displayQuestion()
        setEventClick()

    }

    fun showResult(status: Boolean) {
        var wrongAnswerBackground: Drawable? = null
        var correctAnswerBackground: Drawable? = null

        wrongAnswerBackground = ContextCompat.getDrawable(this,R.drawable.bg_wrong_answer)
        correctAnswerBackground = ContextCompat.getDrawable(this,R.drawable.bg_correct_answer)


        when (listCorrectAnswer?.get(currentQuestion).toInt()) {
            ANSWER_1 -> textAnswerA?.background = correctAnswerBackground
            ANSWER_2 -> textAnswerB?.background = correctAnswerBackground
            ANSWER_3 -> textAnswerC?.background = correctAnswerBackground
            ANSWER_4 -> textAnswerD?.background = correctAnswerBackground
        }

        if (!status) {
            when (pickedAnswer) {
                ANSWER_1 -> textAnswerA?.background = wrongAnswerBackground
                ANSWER_2 -> textAnswerB?.background = wrongAnswerBackground
                ANSWER_3 -> textAnswerC?.background = wrongAnswerBackground
                ANSWER_4 -> textAnswerD?.background = wrongAnswerBackground
            }
        } else correctedAnswersNumber++
    }

    fun displayQuestion() {
        textQuestion.text = listQuestion[currentQuestion]
        displayAnswers()
        resetAnswersBackground()
        hideNextButton()
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

    fun displayAnswers() {
        textAnswerA.text = listAnswerA[currentQuestion]
        textAnswerB.text = listAnswerB[currentQuestion]
        textAnswerC.text = listAnswerC[currentQuestion]
        textAnswerD.text = listAnswerD[currentQuestion]
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

    fun evaluate(answerNumber: Int) {
        pickedAnswer = answerNumber
        showResult(pickedAnswer.equals(listCorrectAnswer[currentQuestion]))
        displayNextButton()
    }
    fun setEventClick() {

        textAnswerA?.setOnClickListener {
            evaluate(ANSWER_1)
        }
        textAnswerB?.setOnClickListener {
            evaluate(ANSWER_2)
        }
        textAnswerC?.setOnClickListener {
            evaluate(ANSWER_3)
        }
        textAnswerD?.setOnClickListener {
            evaluate(ANSWER_4)
        }
    }

    companion object {

        private const val ANSWER_1 = 0
        private const val ANSWER_2 = 1
        private const val ANSWER_3 = 2
        private const val ANSWER_4 = 3
        private const val START_QUESTION = 0

    }
}