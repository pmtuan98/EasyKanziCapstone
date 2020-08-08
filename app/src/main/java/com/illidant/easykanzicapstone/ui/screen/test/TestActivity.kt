package com.illidant.easykanzicapstone.ui.screen.test

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Quiz
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


class TestActivity : AppCompatActivity(), QuizContract.View, TestContract.View  {

    var timeTaken: Int = 0
    var level_id: Int = 0
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    val currentDate = sdf.format(Date())

    val timer = object : CountDownTimer(601000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            edtTimeTest.setText(""+String.format("00:%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))))
            timeTaken = (600 - TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)).toInt()
        }

        override fun onFinish() {
            //Not use
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initialize()

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
        level_id = intent.getIntExtra("LEVEL_ID", 0)
        presenter.quizByLevelRequest(level_id)
        timer.start()
    }

    override fun getQuizByLevelID(listQuiz: List<Quiz>) {
        val listRandomQuiz: List<Quiz> = listQuiz.shuffled().take(30)
        var currentPosition = 0
        var countCorrectAnswer = 0

        //set progress bar
        progressBarMultiple.max = listRandomQuiz.size
        progressBarMultiple.progress = currentPosition+1

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
        var correctAnswer = listRandomQuiz[currentPosition].correctAnswer

        fun submitResult() {
            timer.cancel()
//            val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
//            val userID = prefs.getInt("userID", 0)
//            var score = countCorrectAnswer * 10 / 3
//            val testResultRequest = TestRankingRequest(currentDate.toString(),level_id,score,timeTaken,userID)
//            test_presenter.sendTestResult(testResultRequest)
            val intent = Intent(this, TestResultActivity::class.java)
            intent.putExtra("TOTAL_QUES", listRandomQuiz.size)
            intent.putExtra("TOTAL_CORRECT",countCorrectAnswer)
            startActivity(intent)
            finish()
        }

        fun nextQuestion() {
            currentPosition++
            if (currentPosition == listRandomQuiz.size) {
                currentPosition = listRandomQuiz.size - 1

                Toast.makeText(this,"Correct answer: "+ countCorrectAnswer, Toast.LENGTH_LONG).show()
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

        btnAnswerA?.setOnClickListener {
            if(tvAnswerA.text.equals(correctAnswer)){
                countCorrectAnswer++
            }
            nextQuestion()
        }
        btnAnswerB?.setOnClickListener {
            if(tvAnswerB.text.equals(correctAnswer)){
                countCorrectAnswer++
            }
            nextQuestion()
        }
        btnAnswerC?.setOnClickListener {
            if(tvAnswerC.text.equals(correctAnswer)){
                countCorrectAnswer++
            }
            nextQuestion()
        }
        btnAnswerD?.setOnClickListener {
            if(tvAnswerD.text.equals(correctAnswer)){
                countCorrectAnswer++
            }
            nextQuestion()
        }

        btnSubmit.setOnClickListener {
            timer.cancel()
            var score = countCorrectAnswer * 10 / 3
            val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
            val userID = prefs.getInt("userID", 0)
//          val testResultRequest = TestRankingRequest(currentDate.toString(),level_id,score,timeTaken,userID)
//          test_presenter.sendTestResult(testResultRequest)
            val intent = Intent(it.context, TestResultActivity::class.java)
            intent.putExtra("TOTAL_QUES", listRandomQuiz.size)
            intent.putExtra("TOTAL_CORRECT",countCorrectAnswer)
            startActivity(intent)
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        timer.cancel()
    }

    override fun onSendTestResultSucceeded(message: String) {
        //NOT USE
//        val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
//        dialog.titleText = message
//        dialog.setCancelable(true)
//        dialog.show()
    }

    override fun onSendTestResultFail(message: String) {
        //NOT USE
//        val errDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
//        errDialog.contentText = message
//        errDialog.setCancelable(true)
//        errDialog.show()

    }
    override fun getQuizByLessonID(listQuiz: List<Quiz>) {
        //Not use
    }
}