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
import kotlinx.android.synthetic.main.activity_multiple_choice.tvTotalQuestion

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
        btnExit.setOnClickListener {
            finish()
        }
    }

    private fun showCompleteDialog () {
        val dialog = Dialog(this)
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_complete_multiplechoice)
        val buttonAgain= dialog.findViewById(R.id.btnLearnAgain) as Button
        val buttonLearnWriting = dialog.findViewById(R.id.btnLearnWriting) as Button
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
    fun checkBackQuestion(position: Int) {
        if(position <= 0) {
            btnBackQuestion.isEnabled = false
        }else {
            btnBackQuestion.isEnabled = true
        }
    }
    override fun getQuizByLessonID(listQuiz: List<Quiz>) {
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
        checkBackQuestion(currentPosition)
        //hideButton()

        tvAnswerA?.setOnClickListener {
            checkCorrectAnswer(correctAnswer)
            if(!tvAnswerA.text.equals(correctAnswer)){
                tvAnswerA?.background = wrongAnswerBackground
            }
            displayButton()
        }
        tvAnswerB?.setOnClickListener {
            checkCorrectAnswer(correctAnswer)
            if(!tvAnswerB.text.equals(correctAnswer)){
                tvAnswerB?.background = wrongAnswerBackground
            }
            displayButton()
        }
        tvAnswerC?.setOnClickListener {
            checkCorrectAnswer(correctAnswer)
            if(!tvAnswerC.text.equals(correctAnswer)){
                tvAnswerC?.background = wrongAnswerBackground
            }
            displayButton()
        }
        tvAnswerD?.setOnClickListener {
            checkCorrectAnswer(correctAnswer)
            if(!tvAnswerD.text.equals(correctAnswer)){
                tvAnswerD?.background = wrongAnswerBackground
            }
            displayButton()
        }

        btnNextQuestion.setOnClickListener({
            currentPosition++
            if (currentPosition == listQuiz.size) {
                currentPosition = listQuiz.size - 1
                showCompleteDialog()
            }
            checkBackQuestion(currentPosition)
            tvQuestionNo.text = (currentPosition + 1).toString()
            progressBarMultiple.progress = currentPosition + 1
            tvQuestion.text = listQuiz[currentPosition].question
            tvAnswerA.text = listQuiz[currentPosition].answerA
            tvAnswerB.text = listQuiz[currentPosition].answerB
            tvAnswerC.text = listQuiz[currentPosition].answerC
            tvAnswerD.text = listQuiz[currentPosition].answerD
            correctAnswer = listQuiz[currentPosition].correctAnswer
            resetAnswersBackground()
        })
        btnBackQuestion.setOnClickListener({
            currentPosition--
            if (currentPosition < 0) {
                currentPosition = 0
            }
            checkBackQuestion(currentPosition)
            tvQuestionNo.text = (currentPosition + 1).toString()
            progressBarMultiple.progress = currentPosition+1
            tvQuestion.text = listQuiz[currentPosition].question
            tvAnswerA.text = listQuiz[currentPosition].answerA
            tvAnswerB.text = listQuiz[currentPosition].answerB
            tvAnswerC.text = listQuiz[currentPosition].answerC
            tvAnswerD.text = listQuiz[currentPosition].answerD
            correctAnswer = listQuiz[currentPosition].correctAnswer
            resetAnswersBackground()
        })

    }

    override fun getQuizByLevelID(listQuiz: List<Quiz>) {
       //not use
    }

    fun checkCorrectAnswer(answer : String) {
        var correctAnswerBackground: Drawable? = null
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

    fun resetAnswersBackground() {
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

    fun displayButton() {
        btnNextQuestion?.visibility = View.VISIBLE
        btnBackQuestion?.visibility = View.VISIBLE
        tvAnswerA.isEnabled = false
        tvAnswerB.isEnabled = false
        tvAnswerC.isEnabled = false
        tvAnswerD.isEnabled = false
    }

}