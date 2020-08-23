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
import com.illidant.easykanzicapstone.BaseActivity
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
import kotlinx.android.synthetic.main.dialog_complete_multiplechoice.*

class MultipleChoiceActivity : BaseActivity(), QuizContract.View {

    private var countCorrect = 0
    private var countSelect = 0
    private val listQuizAll: MutableList<Quiz> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple_choice)
        initialize()
        configViews()
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
    private fun configViews() {
        btnExit.setOnClickListener {
            finish()
        }
        btnFinish.setOnClickListener {
            showFinishDialog()
        }
    }

    private fun showCompleteDialog() {
        val dialog = Dialog(this)
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        dialog.setCancelable(false)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_complete_multiplechoice)
        val btnAgain = dialog.findViewById(R.id.btnLearnAgain) as Button
        val btnWriting = dialog.findViewById(R.id.btnWriting) as Button
        val btnFlashcard = dialog.findViewById(R.id.btnFlashcard) as Button
        val btnQuit = dialog.findViewById(R.id.btnQuit) as Button
        val tvTotalQuestion = dialog.findViewById(R.id.tvTotalQuestion) as TextView
        val tvCorrect = dialog.findViewById(R.id.tvCorrect) as TextView
        val tvWrong = dialog.findViewById(R.id.tvWrong) as TextView
        tvTotalQuestion.text = listQuizAll.size.toString()
        tvCorrect.text = countCorrect.toString()
        tvWrong.text = (countSelect - countCorrect).toString()
        dialog.show()
        btnAgain.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
            dialog.dismiss()
        }
        btnWriting.setOnClickListener {
            val intent = Intent(it.context, WritingActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
        btnFlashcard.setOnClickListener {
            val intent = Intent(it.context, FlashcardActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
        btnQuit.setOnClickListener {
            finish()
        }
    }

    private fun showFinishDialog() {
        val dialog = Dialog(this)
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        dialog.setCancelable(true)
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_finish_multiplechoice)
        val btnAgain = dialog.findViewById(R.id.btnLearnAgain) as Button
        val btnWriting = dialog.findViewById(R.id.btnWriting) as Button
        val btnFlashcard = dialog.findViewById(R.id.btnFlashcard) as Button
        val tvTotalQuestion = dialog.findViewById(R.id.tvTotalQuestion) as TextView
        val tvCorrect = dialog.findViewById(R.id.tvCorrect) as TextView
        val tvWrong = dialog.findViewById(R.id.tvWrong) as TextView
        tvTotalQuestion.text = listQuizAll.size.toString()
        tvCorrect.text = countCorrect.toString()
        tvWrong.text = (countSelect - countCorrect).toString()
        dialog.show()
        btnAgain.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
            dialog.dismiss()
        }
        btnWriting.setOnClickListener {
            val intent = Intent(it.context, WritingActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
        btnFlashcard.setOnClickListener {
            val intent = Intent(it.context, FlashcardActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
    }

    private var correctAnswer = ""
    private var currentPosition = 0
    private var wrongAnswerBackground: Drawable? = null

    override fun getQuizByLessonID(listQuiz: List<Quiz>) {
        listQuizAll.addAll(listQuiz.shuffled())
        displayQuestion()
        resetAnswersBackground()
        hideNextButton()
        checkSelection()
        nextQuestion()

    }
    private fun displayQuestion() {
        wrongAnswerBackground = ContextCompat.getDrawable(this, R.drawable.bg_wrong_answer)
        //set progress bar
        progressBarMultiple.max = listQuizAll.size
        progressBarMultiple.progress = currentPosition + 1

        //Display total question
        tvTotalQuestion.text = listQuizAll.size.toString()
        tvQuestionNo.text = (currentPosition + 1).toString()

        // Display question
        tvQuestion.text = listQuizAll[currentPosition].question

        // Display answer
        tvAnswerA.text = listQuizAll[currentPosition].answerA
        tvAnswerB.text = listQuizAll[currentPosition].answerB
        tvAnswerC.text = listQuizAll[currentPosition].answerC
        tvAnswerD.text = listQuizAll[currentPosition].answerD
        correctAnswer = listQuizAll[currentPosition].correctAnswer
    }
    private fun checkSelection() {
        tvAnswerA?.setOnClickListener {
            checkCorrectAnswer(correctAnswer)
            countSelect++
            if (tvAnswerA.text.equals(correctAnswer)) {
                countCorrect++
            } else {
                tvAnswerA?.background = wrongAnswerBackground
            }
            displayNextButton()
        }
        tvAnswerB?.setOnClickListener {
            countSelect++
            checkCorrectAnswer(correctAnswer)
            if (tvAnswerB.text.equals(correctAnswer)) {
                countCorrect++
            } else {
                tvAnswerB?.background = wrongAnswerBackground
            }
            displayNextButton()
        }
        tvAnswerC?.setOnClickListener {
            countSelect++
            checkCorrectAnswer(correctAnswer)
            if (tvAnswerC.text.equals(correctAnswer)) {
                countCorrect++
            } else {
                tvAnswerC?.background = wrongAnswerBackground
            }
            displayNextButton()
        }
        tvAnswerD?.setOnClickListener {
            countSelect++
            checkCorrectAnswer(correctAnswer)
            if (tvAnswerD.text.equals(correctAnswer)) {
                countCorrect++
            } else {
                tvAnswerD?.background = wrongAnswerBackground
            }
            displayNextButton()
        }
    }
    private fun nextQuestion() {
        btnNextQuestion.setOnClickListener({
            currentPosition++
            if (currentPosition == listQuizAll.size) {
                currentPosition = listQuizAll.size - 1
                showCompleteDialog()
            }
            tvQuestionNo.text = (currentPosition + 1).toString()
            progressBarMultiple.progress = currentPosition + 1
            tvQuestion.text = listQuizAll[currentPosition].question
            tvAnswerA.text = listQuizAll[currentPosition].answerA
            tvAnswerB.text = listQuizAll[currentPosition].answerB
            tvAnswerC.text = listQuizAll[currentPosition].answerC
            tvAnswerD.text = listQuizAll[currentPosition].answerD
            correctAnswer = listQuizAll[currentPosition].correctAnswer
            resetAnswersBackground()
            hideNextButton()
        })
    }

    override fun getQuizByLevelID(listQuiz: List<Quiz>) {
        //not use
    }

    private fun checkCorrectAnswer(answer: String) {
        var correctAnswerBackground: Drawable?
        correctAnswerBackground = ContextCompat.getDrawable(this, R.drawable.bg_correct_answer)
        if (tvAnswerA.text.equals(answer)) {
            tvAnswerA?.background = correctAnswerBackground
        } else if (tvAnswerB.text.equals(answer)) {
            tvAnswerB?.background = correctAnswerBackground
        } else if (tvAnswerC.text.equals(answer)) {
            tvAnswerC?.background = correctAnswerBackground
        } else if (tvAnswerD.text.equals(answer)) {
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