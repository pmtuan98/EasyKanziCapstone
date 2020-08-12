package com.illidant.easykanzicapstone.ui.screen.test.show_answer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Quiz
import kotlinx.android.synthetic.main.activity_answer_test.*

class AnswerTestActivity : AppCompatActivity() {
    var listRandomQuiz: List<Quiz> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_test)
        answerTestData()
        returnButton()
    }

    private fun answerTestData() {
        //Send question and answer to show answer screen
        listRandomQuiz = intent.getParcelableArrayListExtra("LIST_QUIZ")
        Log.d("RANDOM", listRandomQuiz.toString())
        recyclerShowAnswer!!.layoutManager = GridLayoutManager(this, 1)
        recyclerShowAnswer!!.adapter = AnswerTestAdapter(listRandomQuiz, this)
    }

    private fun returnButton() {
        btnBack.setOnClickListener {
            finish()
        }
    }
}