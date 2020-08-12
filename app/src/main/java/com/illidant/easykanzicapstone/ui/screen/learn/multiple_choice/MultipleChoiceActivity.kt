package com.illidant.easykanzicapstone.ui.screen.learn.multiple_choice

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
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
import kotlinx.android.synthetic.main.activity_multiple_choice.tvTotalQuestion

class MultipleChoiceActivity : AppCompatActivity(), QuizContract.View {

    private var countCorrect = 0
    val listQuizAll : MutableList<Quiz> = mutableListOf()
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
        btnExit.setOnClickListener {
            finish()
        }
    }

    private fun showCompleteDialog () {
        val dialog = Dialog(this)
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        dialog.setCancelable(false)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_complete_multiplechoice)
        val btnAgain= dialog.findViewById(R.id.btnLearnAgain) as Button
        val btnWriting = dialog.findViewById(R.id.btnWriting) as Button
        val btnFlashcard = dialog.findViewById(R.id.btnFlashcard) as Button
        val tvTotalQuestion= dialog.findViewById(R.id.tvTotalQuestion) as TextView
        val tvCorrect = dialog.findViewById(R.id.tvCorrect) as TextView
        val tvWrong = dialog.findViewById(R.id.tvWrong) as TextView
        tvTotalQuestion.text = listQuizAll.size.toString()
        tvCorrect.text = countCorrect.toString()
        tvWrong.text = (listQuizAll.size-countCorrect).toString()
        dialog.show()
        btnAgain.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
            dialog.dismiss()
        }
        btnWriting.setOnClickListener{
            val intent = Intent(it.context, WritingActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
        btnFlashcard.setOnClickListener{
            val intent = Intent(it.context, FlashcardActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
    }
    override fun getQuizByLessonID(listQuiz: List<Quiz>) {
        listQuizAll.addAll(listQuiz)
        var currentPosition = 0
        var wrongAnswerBackground: Drawable?
        wrongAnswerBackground = ContextCompat.getDrawable(this,R.drawable.bg_wrong_answer)

        //set progress bar
        progressBarMultiple.max = listQuiz.size
        progressBarMultiple.progress = currentPosition + 1

        //Display total question
        tvTotalQuestion.text = listQuiz.size.toString()
        tvQuestionNo.text = (currentPosition + 1).toString()

        // Display question
        tvQuestion.text = listQuiz[currentPosition].question

        // Display answer
        tvAnswerA.text = listQuiz[currentPosition].answerA
        tvAnswerB.text = listQuiz[currentPosition].answerB
        tvAnswerC.text = listQuiz[currentPosition].answerC
        tvAnswerD.text = listQuiz[currentPosition].answerD
        var correctAnswer = listQuiz[currentPosition].correctAnswer
        resetAnswersBackground()
        hideNextButton()

        tvAnswerA?.setOnClickListener {
            checkCorrectAnswer(correctAnswer)
            if(tvAnswerA.text.equals(correctAnswer)){
                countCorrect++
            }else {
                tvAnswerA?.background = wrongAnswerBackground
            }
            displayNextButton()
        }
        tvAnswerB?.setOnClickListener {
            checkCorrectAnswer(correctAnswer)
            if(tvAnswerB.text.equals(correctAnswer)){
                countCorrect++
            }else {
                tvAnswerB?.background = wrongAnswerBackground
            }
            displayNextButton()
        }
        tvAnswerC?.setOnClickListener {
            checkCorrectAnswer(correctAnswer)
            if(tvAnswerC.text.equals(correctAnswer)){
                countCorrect++
            }else {
                tvAnswerC?.background = wrongAnswerBackground
            }
            displayNextButton()
        }
        tvAnswerD?.setOnClickListener {
            checkCorrectAnswer(correctAnswer)
            if(tvAnswerD.text.equals(correctAnswer)){
                countCorrect++
            }else {
                tvAnswerD?.background = wrongAnswerBackground
            }
            displayNextButton()
        }

        btnNextQuestion.setOnClickListener({
            currentPosition++
            if (currentPosition == listQuiz.size) {
                currentPosition = listQuiz.size - 1
                showCompleteDialog()
            }
            tvQuestionNo.text = (currentPosition + 1).toString()
            progressBarMultiple.progress = currentPosition + 1
            tvQuestion.text = listQuiz[currentPosition].question
            tvAnswerA.text = listQuiz[currentPosition].answerA
            tvAnswerB.text = listQuiz[currentPosition].answerB
            tvAnswerC.text = listQuiz[currentPosition].answerC
            tvAnswerD.text = listQuiz[currentPosition].answerD
            correctAnswer = listQuiz[currentPosition].correctAnswer
            resetAnswersBackground()
            hideNextButton()
        })
    }

    override fun getQuizByLevelID(listQuiz: List<Quiz>) {
       //not use
    }

    private fun checkCorrectAnswer(answer : String) {
        var correctAnswerBackground: Drawable?
        correctAnswerBackground = ContextCompat.getDrawable(this,R.drawable.bg_correct_answer)
        if(tvAnswerA.text.equals(answer)){
            tvAnswerA?.background = correctAnswerBackground
        }else if (tvAnswerB.text.equals(answer)){
            tvAnswerB?.background = correctAnswerBackground
        }else if (tvAnswerC.text.equals(answer)){
            tvAnswerC?.background = correctAnswerBackground
        }else if (tvAnswerD.text.equals(answer)){
            tvAnswerD?.background = correctAnswerBackground
        }
    }

    private fun resetAnswersBackground() {
        val defaultAnswerBackground = ContextCompat.getDrawable(this, R.drawable.bg_detail_kanji)
        tvAnswerA?.background = defaultAnswerBackground
        tvAnswerB?.background = defaultAnswerBackground
        tvAnswerC?.background = defaultAnswerBackground
        tvAnswerD?.background = defaultAnswerBackground

        tvAnswerA.isEnabled = true
        tvAnswerB.isEnabled = true
        tvAnswerC.isEnabled = true
        tvAnswerD.isEnabled = true
    }

    private fun displayNextButton() {
        btnNextQuestion?.visibility = View.VISIBLE
        tvAnswerA.isEnabled = false
        tvAnswerB.isEnabled = false
        tvAnswerC.isEnabled = false
        tvAnswerD.isEnabled = false
    }
    private fun hideNextButton() {
        btnNextQuestion?.visibility = View.INVISIBLE
    }


}