package com.illidant.easykanzicapstone.ui.screen.test.show_answer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Quiz
import kotlinx.android.synthetic.main.item_test_result.view.*

class AnswerTestAdapter: RecyclerView.Adapter<AnswerTestAdapter.AnswerView> {
    var listQuiz: List<Quiz>? = null
    var context: Context

    constructor(listQuiz: List<Quiz>?, context: Context) : super() {
        this.listQuiz = listQuiz
        this.context = context
    }

    class AnswerView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvStt: TextView
        var tvQuestion: TextView
        var tvAnswerA: TextView
        var tvAnswerB: TextView
        var tvAnswerC: TextView
        var tvAnswerD: TextView

        init {
            tvStt = itemView.tvStt as TextView
            tvQuestion = itemView.tvQuestion as TextView
            tvAnswerA = itemView.tvAnswerA as TextView
            tvAnswerB = itemView.tvAnswerB as TextView
            tvAnswerC = itemView.tvAnswerC as TextView
            tvAnswerD = itemView.tvAnswerD as TextView


        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AnswerView {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_test_result, viewGroup, false)
        return AnswerView(view)
    }

    override fun getItemCount(): Int {
        return listQuiz!!.size
    }

    override fun onBindViewHolder(view: AnswerView, position: Int) {
        view.tvStt.text = (position + 1).toString()
        view.tvQuestion.text = listQuiz?.get(position)?.question
        view.tvAnswerA.text = listQuiz?.get(position)?.answerA
        view.tvAnswerB.text = listQuiz?.get(position)?.answerB
        view.tvAnswerC.text = listQuiz?.get(position)?.answerC
        view.tvAnswerD.text = listQuiz?.get(position)?.answerD
    }
}