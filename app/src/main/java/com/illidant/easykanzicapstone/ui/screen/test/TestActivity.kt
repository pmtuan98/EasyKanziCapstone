package com.illidant.easykanzicapstone.ui.screen.test

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.illidant.easykanzicapstone.BaseActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Quiz
import com.illidant.easykanzicapstone.domain.model.ResultQuiz
import com.illidant.easykanzicapstone.domain.request.TestRankingRequest
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.QuizRepository
import com.illidant.easykanzicapstone.platform.repository.TestRepository
import com.illidant.easykanzicapstone.platform.source.remote.QuizRemoteDataSource
import com.illidant.easykanzicapstone.platform.source.remote.TestRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.quiz.QuizContract
import com.illidant.easykanzicapstone.ui.screen.quiz.QuizPresenter
import com.illidant.easykanzicapstone.util.PausableCountDownTimer
import kotlinx.android.synthetic.main.activity_test.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class TestActivity : BaseActivity(), QuizContract.View, TestContract.View {
    private var takenMinutes = 0
    private var takenSeconds = 0
    private var takenMinutesString: String = ""
    private var takenSecondsString: String = ""
    private var timeTaken = 0
    private var levelId = 0
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val currentDate = sdf.format(Date())
    private var listRandomQuiz: List<Quiz> = mutableListOf()
    private val quizResultList = mutableListOf<ResultQuiz>()
    private var currentPosition = 0
    private var countCorrectAnswer = 0
    private lateinit var correctAnswer: String
    private var seconds = 0
    private var minutes = 0
    private var timerValue = 601000
    private var timeLeft: Long = 0

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = QuizRemoteDataSource(retrofit)
        val repository = QuizRepository(remote)
        QuizPresenter(this, repository)
    }

    private val testPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = TestRemoteDataSource(retrofit)
        val repository = TestRepository(remote)
        TestPresenter(this, repository)
    }

    private val timer = object : PausableCountDownTimer(timerValue.toLong(), 100) {
        override fun onTick(millisUntilFinished: Long) {
            timeLeft = millisUntilFinished
            if (millisUntilFinished <= 100) {
                edtTimeTest.text = getString(R.string.text_default_timer)
            } else {
                seconds = (millisUntilFinished / 1000).toInt()
                minutes = seconds / 60
                seconds %= 60
                edtTimeTest.text = getString(R.string.text_timer, minutes, seconds)

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
        configViews()
    }

    private fun initialize() {
        levelId = intent.getIntExtra("LEVEL_ID", 0)
        presenter.quizByLevelRequest(levelId)
        timer.start()
    }

    private fun configViews() {
        btnExit.setOnClickListener { onBackPressed() }
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
        quizResultList.add(ResultQuiz(listRandomQuiz[currentPosition], ""))
    }

    private fun checkCorrectAnswer() {
        val listener = View.OnClickListener {
            val text = (it as TextView).text.toString()
            quizResultList[currentPosition].selectedAnswer = text
            if (text == correctAnswer) {
                countCorrectAnswer++
            }
            nextQuestion()
        }
        tvAnswerA.setOnClickListener(listener)
        tvAnswerB.setOnClickListener(listener)
        tvAnswerC.setOnClickListener(listener)
        tvAnswerD.setOnClickListener(listener)
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
        quizResultList.add(ResultQuiz(listRandomQuiz[currentPosition], ""))
    }

    private fun submitResult() {
        timer.cancel()
        formatTimeTaken()
        val prefs: SharedPreferences =
            getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        val userID = prefs.getInt("userID", 0)
        val score = countCorrectAnswer * 10 / 3
        val testResultRequest =
            TestRankingRequest(currentDate.toString(), levelId, score, timeTaken, userID)
//        testPresenter.sendTestResult(testResultRequest)
        val levelName = intent.getStringExtra("LEVEL_NAME")
        val intent = Intent(this, ResultTestActivity::class.java).apply {
            putExtra("TOTAL_QUES", listRandomQuiz.size)
            putExtra("TOTAL_CORRECT", countCorrectAnswer)
            putExtra("LEVEL_NAME", levelName)
            putParcelableArrayListExtra("LIST_QUIZ", ArrayList(listRandomQuiz))
            putExtra("TAKEN_MINUTES", takenMinutesString)
            putExtra("TAKEN_SECONDS", takenSecondsString)
            putParcelableArrayListExtra("QUIZ_RESULT", ArrayList(quizResultList))
        }
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
        timer.pause()
        val dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = "Quit current test?"
        dialog.contentText = "All results will be lost!"
        dialog.setCancelable(false)
        dialog.cancelText = "Cancel"
        dialog.confirmText = "Quit"
        dialog.show()
        dialog.setConfirmClickListener {
            super.onBackPressed()
        }
        dialog.setCancelClickListener {
            dialog.dismiss()
            timer.resume()
        }
    }

    override fun onSendTestResultSucceeded(message: String) {
        //Not use
    }

    override fun onSendTestResultFail(message: String) {
        //NOT USE
    }

    override fun getQuizByLessonID(listQuiz: List<Quiz>) {
        //Not use
    }
}