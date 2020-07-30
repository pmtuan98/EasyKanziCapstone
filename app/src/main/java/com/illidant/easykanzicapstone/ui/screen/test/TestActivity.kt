package com.illidant.easykanzicapstone.ui.screen.test

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import kotlinx.android.synthetic.main.activity_multiple_choice.progressBarMultiple
import kotlinx.android.synthetic.main.activity_multiple_choice.textAnswerA
import kotlinx.android.synthetic.main.activity_multiple_choice.textAnswerB
import kotlinx.android.synthetic.main.activity_multiple_choice.textAnswerC
import kotlinx.android.synthetic.main.activity_multiple_choice.textAnswerD
import kotlinx.android.synthetic.main.activity_multiple_choice.textQuestion
import kotlinx.android.synthetic.main.activity_multiple_choice.tv_questionNo
import kotlinx.android.synthetic.main.activity_multiple_choice.tv_totalQuestion
import kotlinx.android.synthetic.main.activity_test.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class TestActivity : AppCompatActivity(), QuizContract.View, TestContract.View  {

    var timeTaken: Long = 0
    var level_id: Int = 0
    val sdf = SimpleDateFormat("dd/M/yyyy")
    val currentDate = sdf.format(Date())
    var formatTimeTaken: String = ""

    val timer = object : CountDownTimer(601000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            edtTimeTest.setText(""+String.format("%d : %d",
                TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))))
            timeTaken = 600 - TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
            formatTimeTaken = String.format("%d : %d",
                TimeUnit.MILLISECONDS.toMinutes( timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken)))
        }

        override fun onFinish() {

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

    override fun getQuizByLessonID(listQuiz: List<Quiz>) {
        //Not use
    }

    override fun getQuizByLevelID(listQuiz: List<Quiz>) {
        val listRandomQuiz: List<Quiz> = listQuiz.shuffled().take(30)
        var currentPosition = 0
        var countCorrectAnswer = 0
        var chooseAnswerBackground = ContextCompat.getDrawable(this,R.drawable.bg_correct_answer)
        //set progress bar
        progressBarMultiple.max = listRandomQuiz.size
        progressBarMultiple.progress = currentPosition

        //Display total question
        tv_totalQuestion.text = listRandomQuiz.size.toString()
        tv_questionNo.text = (currentPosition + 1).toString()

        // Display question
        textQuestion.text = listRandomQuiz[currentPosition].question

        // Display answer
        textAnswerA.text = listRandomQuiz[currentPosition].answerA
        textAnswerB.text = listRandomQuiz[currentPosition].answerB
        textAnswerC.text = listRandomQuiz[currentPosition].answerC
        textAnswerD.text = listRandomQuiz[currentPosition].answerD
        var correctAnswer = listRandomQuiz[currentPosition].correctAnswer

        fun resetAnswersBackground() {
            val defaultAnswerBackground = ContextCompat.getDrawable(this, R.drawable.bg_detail_kanji)
            textAnswerA?.background = defaultAnswerBackground
            textAnswerB?.background = defaultAnswerBackground
            textAnswerC?.background = defaultAnswerBackground
            textAnswerD?.background = defaultAnswerBackground
        }

        fun nextQuestion() {
            resetAnswersBackground()
            currentPosition++
            if (currentPosition == listRandomQuiz.size) {
                currentPosition = listRandomQuiz.size - 1
                // showCompleteDialog()
                Toast.makeText(this,"Correct answer: "+ countCorrectAnswer, Toast.LENGTH_LONG).show()
            }
            tv_questionNo.text = (currentPosition + 1).toString()
            progressBarMultiple.progress = currentPosition + 1
            textQuestion.text = listRandomQuiz[currentPosition].question
            textAnswerA.text = listRandomQuiz[currentPosition].answerA
            textAnswerB.text = listRandomQuiz[currentPosition].answerB
            textAnswerC.text = listRandomQuiz[currentPosition].answerC
            textAnswerD.text = listRandomQuiz[currentPosition].answerD
            correctAnswer = listRandomQuiz[currentPosition].correctAnswer
        }

        textAnswerA?.setOnClickListener {
            textAnswerA?.background = chooseAnswerBackground
            if(textAnswerA.text.equals(correctAnswer)){
                countCorrectAnswer++
            }
            nextQuestion()
        }
        textAnswerB?.setOnClickListener {
            textAnswerB?.background = chooseAnswerBackground
            if(textAnswerB.text.equals(correctAnswer)){
                countCorrectAnswer++
            }
            nextQuestion()
        }
        textAnswerC?.setOnClickListener {
            textAnswerC?.background = chooseAnswerBackground
            if(textAnswerC.text.equals(correctAnswer)){
                countCorrectAnswer++
            }
            nextQuestion()
        }
        textAnswerD?.setOnClickListener {
            textAnswerD?.background = chooseAnswerBackground
            if(textAnswerD.text.equals(correctAnswer)){
                countCorrectAnswer++
            }
            nextQuestion()
        }

        btnSubmit.setOnClickListener {
            timer.cancel()
            var score = countCorrectAnswer * 10 / 3
            val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
            val userID = prefs.getInt("userID", 0)
//            val testResultRequest = TestRankingRequest(currentDate.toString(),level_id,score.toString(),formatTimeTaken,userID)
//            test_presenter.sendTestResult(testResultRequest)
            Log.d("TIME", formatTimeTaken)

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        timer.cancel()
    }

    override fun onSendTestResultSucceeded(message: String) {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = message
        dialog.setCancelable(true)
        dialog.show()
    }

    override fun onSendTestResultFail(message: String) {
        val errDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        errDialog.contentText = message
        errDialog.setCancelable(true)
        errDialog.show()
    }
}