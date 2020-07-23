package com.illidant.easykanzicapstone.ui.screen.learn.multiple_choice

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
    var position = 0
    val listQuestion : MutableList<String> = mutableListOf()
    val listAnswerA : MutableList<String> = mutableListOf()
    val listAnswerB : MutableList<String> = mutableListOf()
    val listAnswerC : MutableList<String> = mutableListOf()
    val listAnswerD : MutableList<String> = mutableListOf()
    val listCorrectAnswer: MutableList<String> = mutableListOf()
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
        val lesson_name = intent.getStringExtra("LESSON_NAME")
        val level_name = intent.getStringExtra("LEVEL_NAME")
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

        textQuestion.text = listQuestion[position]
        textAnswerA.text = listAnswerA[position]
        textAnswerB.text = listAnswerB[position]
        textAnswerC.text = listAnswerC[position]
        textAnswerD.text = listAnswerD[position]
        val correctAnswer = listCorrectAnswer[position]

        textAnswerA.setOnClickListener({
            if(textAnswerA.text.equals(correctAnswer)){
                textAnswerA.background = ContextCompat.getDrawable(this, R.drawable.bg_correct_answer)
                btnNextQuestion.visibility = View.VISIBLE
                textAnswerB.isClickable = false
                textAnswerC.isClickable = false
                textAnswerD.isClickable = false
            }else {
                textAnswerA.background = ContextCompat.getDrawable(this, R.drawable.bg_wrong_answer)
                textAnswerB.isClickable = false
                textAnswerC.isClickable = false
                textAnswerD.isClickable = false
                if(textAnswerA.text.equals(correctAnswer)){
                    textAnswerA.background = ContextCompat.getDrawable(this, R.drawable.bg_correct_answer)
                    btnNextQuestion.visibility = View.VISIBLE
                }else if(textAnswerB.text.equals(correctAnswer)){
                    textAnswerB.background = ContextCompat.getDrawable(this, R.drawable.bg_correct_answer)
                    btnNextQuestion.visibility = View.VISIBLE
                }else if(textAnswerC.text.equals(correctAnswer)){
                    textAnswerC.background = ContextCompat.getDrawable(this, R.drawable.bg_correct_answer)
                    btnNextQuestion.visibility = View.VISIBLE
                } else if(textAnswerD.text.equals(correctAnswer)){
                    textAnswerD.background = ContextCompat.getDrawable(this, R.drawable.bg_correct_answer)
                    btnNextQuestion.visibility = View.VISIBLE
                }
            }
        })

        textAnswerB.setOnClickListener({
            if(textAnswerB.text.equals(correctAnswer)){
                textAnswerB.background = ContextCompat.getDrawable(this, R.drawable.bg_correct_answer)
                btnNextQuestion.visibility = View.VISIBLE
            }else {
                textAnswerB.background = ContextCompat.getDrawable(this, R.drawable.bg_wrong_answer)
            }
        })

        textAnswerC.setOnClickListener({
            if(textAnswerC.text.equals(correctAnswer)){
                textAnswerC.background = ContextCompat.getDrawable(this, R.drawable.bg_correct_answer)
                btnNextQuestion.visibility = View.VISIBLE
            }else {
                textAnswerC.background = ContextCompat.getDrawable(this, R.drawable.bg_wrong_answer)
            }
        })

        textAnswerD.setOnClickListener({
            if(textAnswerD.text.equals(correctAnswer)){
                textAnswerD.background = ContextCompat.getDrawable(this, R.drawable.bg_correct_answer)
                btnNextQuestion.visibility = View.VISIBLE
            }else {
                textAnswerD.background = ContextCompat.getDrawable(this, R.drawable.bg_wrong_answer)
            }
        })

    }
}