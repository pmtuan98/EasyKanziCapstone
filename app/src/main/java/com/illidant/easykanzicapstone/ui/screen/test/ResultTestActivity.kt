package com.illidant.easykanzicapstone.ui.screen.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.illidant.easykanzicapstone.BaseActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Quiz
import com.illidant.easykanzicapstone.ui.screen.kanji.KanjiDetailActivity
import com.illidant.easykanzicapstone.ui.screen.test.show_answer.AnswerTestActivity
import com.illidant.easykanzicapstone.ui.screen.test.show_answer.AnswerTestAdapter
import kotlinx.android.synthetic.main.activity_answer_test.*
import kotlinx.android.synthetic.main.activity_test_result.*

class ResultTestActivity : BaseActivity() {
    private var totalQuestion = 0
    private var totalCorrect = 0
    private var takenMinutes: String = ""
    private var takenSeconds: String = ""
    var listRandomQuiz: List<Quiz> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_result)
        initialize()
    }

    private fun initialize() {
        tvLevel.text = intent.getStringExtra("LEVEL_NAME")
        totalQuestion = intent.getIntExtra("TOTAL_QUES", 0)
        totalCorrect = intent.getIntExtra("TOTAL_CORRECT", 0)
        takenMinutes = intent.getStringExtra("TAKEN_MINUTES")
        takenSeconds = intent.getStringExtra("TAKEN_SECONDS")
        showTestResult()
        navigateToShowAnwer()
    }

    private fun showTestResult() {
        var rate = totalCorrect * 100 / totalQuestion
        titleCorrectAnswer.text = totalCorrect.toString()
        tvTotalQuestion.text = totalQuestion.toString()
        tvCorrectRate.text = "${rate}%"
        resultProgressbar.max = totalQuestion
        resultProgressbar.progress = totalCorrect
        tvResultTime.text = "${takenMinutes}m : ${takenSeconds}s"
        if (rate <= 50) {
            resultProgressbar.progressDrawable =
                ContextCompat.getDrawable(this, R.drawable.custom_progressbar_low)
        } else if (rate > 50 && rate < 80) {
            resultProgressbar.progressDrawable =
                ContextCompat.getDrawable(this, R.drawable.custom_progressbar_mid)
            tvResultBottom.text = "Quite good!!"
        } else if (rate >= 80 && rate < 100) {
            resultProgressbar.progressDrawable =
                ContextCompat.getDrawable(this, R.drawable.custom_progressbar_high)
            tvResultBottom.text = "Very good!!"
        } else {
            tvResultAbove.text = "Congratulations! You've reached the highest score"
            tvResultBottom.text = "Excellent"
        }
    }

    private fun navigateToShowAnwer() {
        btnShowAnswer.setOnClickListener {
            listRandomQuiz = intent.getParcelableArrayListExtra("LIST_QUIZ")
            val intent = Intent(it.context, AnswerTestActivity::class.java)
            intent.putParcelableArrayListExtra("LIST_QUIZ", ArrayList(listRandomQuiz))
            startActivity(intent)
        }
    }
}