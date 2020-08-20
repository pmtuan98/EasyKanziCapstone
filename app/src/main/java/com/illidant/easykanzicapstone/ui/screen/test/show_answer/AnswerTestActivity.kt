package com.illidant.easykanzicapstone.ui.screen.test.show_answer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Quiz
import com.illidant.easykanzicapstone.domain.model.ResultQuiz
import kotlinx.android.synthetic.main.activity_answer_test.*

class AnswerTestActivity : AppCompatActivity() {
    var listRandomQuiz = mutableListOf<ResultQuiz>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_test)
        answerTestData()
        returnButton()
    }

    private fun answerTestData() {
        //Send question and answer to show answer screen
        listRandomQuiz = intent.getParcelableArrayListExtra<ResultQuiz>("QUIZ_RESULT")
        recyclerShowAnswer?.apply{
            layoutManager = GridLayoutManager(this@AnswerTestActivity, 1)
            adapter = AnswerTestAdapter(listRandomQuiz.toList())
        }
    }

    private fun returnButton() {
        btnBack.setOnClickListener {
            finish()
        }
    }
}