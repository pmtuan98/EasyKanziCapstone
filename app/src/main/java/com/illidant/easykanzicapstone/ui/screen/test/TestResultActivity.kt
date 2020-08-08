package com.illidant.easykanzicapstone.ui.screen.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.illidant.easykanzicapstone.R
import kotlinx.android.synthetic.main.activity_test_result.*

class TestResultActivity : AppCompatActivity() {
    var totalQuestion = 0
    var totalCorrect = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_result)
        initialize()
    }

    private fun initialize() {
        totalQuestion = intent.getIntExtra("TOTAL_QUES", 0)
        totalCorrect = intent.getIntExtra("TOTAL_CORRECT", 0)
        showTestResult()
    }

    private fun showTestResult(){
        var rate = totalCorrect*100 / totalQuestion
        titleCorrectAnswer.text = totalCorrect.toString()
        tvTotalQuestion.text = totalQuestion.toString()
        tvCorrectRate.text = "${rate}%"
        resultProgressbar.max = totalQuestion
        resultProgressbar.progress = totalCorrect
        if(rate <=50) {
            resultProgressbar.progressDrawable = ContextCompat.getDrawable(this, R.drawable.custom_progressbar_low)
        }else if (rate > 30 && rate < 70) {
            resultProgressbar.progressDrawable = ContextCompat.getDrawable(this, R.drawable.custom_progressbar_mid)
        } else {
            resultProgressbar.progressDrawable = ContextCompat.getDrawable(this, R.drawable.custom_progressbar_high)
        }
    }
}