package com.illidant.easykanzicapstone.ui.screen.test

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Quiz
import com.illidant.easykanzicapstone.domain.request.TestRankingRequest
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.QuizRepository
import com.illidant.easykanzicapstone.platform.repository.TestRepository
import com.illidant.easykanzicapstone.platform.source.remote.QuizRemoteDataSource
import com.illidant.easykanzicapstone.platform.source.remote.TestRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.quiz.QuizContract
import com.illidant.easykanzicapstone.ui.screen.quiz.QuizPresenter
import kotlinx.android.synthetic.main.activity_test.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class TestActivity : AppCompatActivity(), QuizContract.View, TestContract.View {
    private var takenMinutes = 0
    private var takenSeconds = 0
    private var takenMinutesString: String = ""
    private var takenSecondsString: String = ""
    private var timeTaken = 0
    private var levelId = 0
    private val sdf = SimpleDateFormat("dd/MM/yyyy")
    private val currentDate = sdf.format(Date())
    private var listRandomQuiz: List<Quiz> = mutableListOf()
    private var currentPosition = 0
    private var countCorrectAnswer = 0
    private lateinit var correctAnswer: String
    private var seconds = 0
    private var minutes = 0
    private var timerValue = 601000
    private var timeLeft: Long = 0

    private val timer = object : CountDownTimer(timerValue.toLong(), 100) {
        override fun onTick(millisUntilFinished: Long) {
            timeLeft = millisUntilFinished
            if (millisUntilFinished <= 100) {
                edtTimeTest.setText("00:00")
            } else {
                seconds = (millisUntilFinished / 1000).toInt()
                minutes = seconds / 60
                seconds = seconds % 60
                edtTimeTest.setText(
                    String.format("%02d", minutes) + ":" + String.format(
                        "%02d",
                        seconds
                    )
                )

                timeTaken = (600 - TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)).toInt()
            }

        }

        override fun onFinish() {
            //Use to send result when time is up
            submitResult()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initialize()
//        configViews()

    }

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = QuizRemoteDataSource(retrofit)
        val repository = QuizRepository(remote)
        QuizPresenter(this, repository)
    }

    private val test_presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = TestRemoteDataSource(retrofit)
        val repository = TestRepository(remote)
        TestPresenter(this, repository)
    }

    private fun initialize() {
        levelId = intent.getIntExtra("LEVEL_ID", 0)
        presenter.quizByLevelRequest(levelId)
        timer.start()
    }

    override fun getQuizByLevelID(listQuiz: List<Quiz>) {
        listRandomQuiz = listQuiz.shuffled().take(30)
        displayTestScreen()
        checkCorrectAnswer()
    }

    private fun displayTestScreen() {
        //set progress bar
        progressBarMultiple.max = listRandomQuiz.size
        progressBarMultiple.progress = currentPosition + 1

        //Display total question
        tvTotalQuestion.text = listRandomQuiz.size.toString()
        tvQuestionNo.text = (currentPosition + 1).toString()

        // Display question
        tvQuestion.text = listRandomQuiz[currentPosition].question

        // Display answer
        tvAnswerA.text = listRandomQuiz[currentPosition].answerA
        tvAnswerB.text = listRandomQuiz[currentPosition].answerB
        tvAnswerC.text = listRandomQuiz[currentPosition].answerC
        tvAnswerD.text = listRandomQuiz[currentPosition].answerD
        correctAnswer = listRandomQuiz[currentPosition].correctAnswer
    }

    private fun checkCorrectAnswer() {
        btnAnswerA?.setOnClickListener {
            if (tvAnswerA.text.equals(correctAnswer)) {
                countCorrectAnswer++
            }
            nextQuestion()
        }
        btnAnswerB?.setOnClickListener {
            if (tvAnswerB.text.equals(correctAnswer)) {
                countCorrectAnswer++
            }
            nextQuestion()
        }
        btnAnswerC?.setOnClickListener {
            if (tvAnswerC.text.equals(correctAnswer)) {
                countCorrectAnswer++
            }
            nextQuestion()
        }
        btnAnswerD?.setOnClickListener {
            if (tvAnswerD.text.equals(correctAnswer)) {
                countCorrectAnswer++
            }
            nextQuestion()
        }
    }

    private fun nextQuestion() {
        currentPosition++
        if (currentPosition == listRandomQuiz.size) {
            currentPosition = listRandomQuiz.size - 1
            submitResult()
        }
        tvQuestionNo.text = (currentPosition + 1).toString()
        progressBarMultiple.progress = currentPosition + 1
        tvQuestion.text = listRandomQuiz[currentPosition].question
        tvAnswerA.text = listRandomQuiz[currentPosition].answerA
        tvAnswerB.text = listRandomQuiz[currentPosition].answerB
        tvAnswerC.text = listRandomQuiz[currentPosition].answerC
        tvAnswerD.text = listRandomQuiz[currentPosition].answerD
        correctAnswer = listRandomQuiz[currentPosition].correctAnswer
    }

    private fun submitResult() {
        timer.cancel()
        formatTimeTaken()
            val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
            val userID = prefs.getInt("userID", 0)
            var score = countCorrectAnswer * 10 / 3
            val testResultRequest = TestRankingRequest(currentDate.toString(),levelId,score,timeTaken,userID)
            test_presenter.sendTestResult(testResultRequest)

        val intent = Intent(this, ResultTestActivity::class.java)
        val levelName = intent.getStringExtra("LEVEL_NAME")

        intent.putExtra("TOTAL_QUES", listRandomQuiz.size)
        intent.putExtra("TOTAL_CORRECT", countCorrectAnswer)
        intent.putExtra("LEVEL_NAME", levelName)
        intent.putParcelableArrayListExtra("LIST_QUIZ", ArrayList(listRandomQuiz))
        intent.putExtra("TAKEN_MINUTES", takenMinutesString)
        intent.putExtra("TAKEN_SECONDS", takenSecondsString)

        startActivity(intent)
        finish()

    }

    private fun formatTimeTaken() {
        takenMinutes = timeTaken / 60
        takenSeconds = timeTaken % 60
        takenMinutesString = String.format("%02d", takenMinutes)
        takenSecondsString = String.format("%02d", takenSeconds)
    }

    override fun onBackPressed() {
        timer.cancel()
        val dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = "Quit current test?"
        dialog.contentText = "All results will be lost!"
        dialog.setCancelable(false)
        dialog.setCancelText("Cancel")
        dialog.setConfirmText("Quit")
        dialog.show()
        dialog.setConfirmClickListener {
            super.onBackPressed()
        }
        dialog.setCancelClickListener {
            dialog.dismiss()
        }
    }

    override fun onSendTestResultSucceeded(message: String) {
//        send test result to server
        val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = message
        dialog.setCancelable(true)
        dialog.show()
    }

    override fun onSendTestResultFail(message: String) {
        //NOT USE
        val errDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        errDialog.contentText = message
        errDialog.setCancelable(true)
        errDialog.show()

    }

    override fun getQuizByLessonID(listQuiz: List<Quiz>) {
        //Not use
    }
}