package com.illidant.easykanzicapstone.ui.screen.test

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.illidant.easykanzicapstone.BaseActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.ResultQuiz
import com.illidant.easykanzicapstone.ui.screen.home.HomeActivity
import com.illidant.easykanzicapstone.ui.screen.test.show_answer.AnswerTestActivity
import kotlinx.android.synthetic.main.activity_test_result.*

class ResultTestActivity : BaseActivity() {
    private var totalQuestion = 0
    private var totalCorrect = 0
    private var takenMinutes: String = ""
    private var takenSeconds: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_result)
        initialize()
        configViews()
    }

    private fun initialize() {
        tvLevel.text = intent.getStringExtra("LEVEL_NAME")
        totalQuestion = intent.getIntExtra("TOTAL_QUES", 0)
        totalCorrect = intent.getIntExtra("TOTAL_CORRECT", 0)
        takenMinutes = intent.getStringExtra("TAKEN_MINUTES")
        takenSeconds = intent.getStringExtra("TAKEN_SECONDS")
        showTestResult()
        navigateToShowAnswer()
    }

    private fun showTestResult() {
        val rate = totalCorrect * 100 / totalQuestion
        titleCorrectAnswer.text = totalCorrect.toString()
        tvTotalQuestion.text = totalQuestion.toString()
        tvCorrectRate.text = "$rate %"
        resultProgressbar.max = totalQuestion
        resultProgressbar.progress = totalCorrect
        tvResultTime.text = "${takenMinutes}m : ${takenSeconds}s"
        when {
            rate <= 50 -> {
                resultProgressbar.progressDrawable =
                    ContextCompat.getDrawable(this, R.drawable.custom_progressbar_low)
            }
            rate in 51..79 -> {
                resultProgressbar.progressDrawable =
                    ContextCompat.getDrawable(this, R.drawable.custom_progressbar_mid)
                tvResultBottom.text = "Quite good!!"
            }
            rate in 80..99 -> {
                resultProgressbar.progressDrawable =
                    ContextCompat.getDrawable(this, R.drawable.custom_progressbar_high)
                tvResultBottom.text = "Very good!!"
            }
            else -> {
                tvResultAbove.text = "Congratulations! You've reached the highest score"
                tvResultBottom.text = "Excellent"
            }
        }
    }

    private fun navigateToShowAnswer() {
        btnShowAnswer.setOnClickListener {
            val quizResultList = intent.getParcelableArrayListExtra<ResultQuiz>("QUIZ_RESULT")
            val intent = Intent(it.context, AnswerTestActivity::class.java)
            quizResultList?.let { list ->
                intent.putParcelableArrayListExtra("QUIZ_RESULT", ArrayList(list))
            }
            startActivity(intent)
        }
    }

    private fun configViews() {
        btnBack.setOnClickListener {
            onBackPressed()
        }
        btnFab.setOnClickListener {
            val intent = Intent(it.context, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}