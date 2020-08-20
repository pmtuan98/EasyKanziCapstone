package com.illidant.easykanzicapstone.ui.screen.test.show_answer

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Quiz
import kotlinx.android.synthetic.main.activity_multiple_choice.*
import kotlinx.android.synthetic.main.item_test_result.view.*

class AnswerTestAdapter : RecyclerView.Adapter<AnswerTestAdapter.AnswerView> {
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
        var sttAnswerA: TextView
        var sttAnswerB: TextView
        var sttAnswerC: TextView
        var sttAnswerD: TextView
        init {
            tvStt = itemView.tvStt as TextView
            tvQuestion = itemView.tvQuestion as TextView
            tvAnswerA = itemView.tvAnswerA as TextView
            tvAnswerB = itemView.tvAnswerB as TextView
            tvAnswerC = itemView.tvAnswerC as TextView
            tvAnswerD = itemView.tvAnswerD as TextView
            sttAnswerA = itemView.sttAnswerA as TextView
            sttAnswerB = itemView.sttAnswerB as TextView
            sttAnswerC = itemView.sttAnswerC as TextView
            sttAnswerD = itemView.sttAnswerD as TextView
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AnswerView {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_test_result, viewGroup, false)
        return AnswerView(view)
    }

    override fun getItemCount(): Int {
        return listQuiz!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(view: AnswerView, position: Int) {

        view.tvStt.text = (position + 1).toString()
        view.tvQuestion.text = listQuiz?.get(position)?.question
        view.tvAnswerA.text = listQuiz?.get(position)?.answerA
        view.tvAnswerB.text = listQuiz?.get(position)?.answerB
        view.tvAnswerC.text = listQuiz?.get(position)?.answerC
        view.tvAnswerD.text = listQuiz?.get(position)?.answerD

            var answer = listQuiz?.get(position)?.correctAnswer
            var correctAnswerBackground: Drawable?
            var wrongAnswerBackground: Drawable?
            correctAnswerBackground = ContextCompat.getDrawable(context, R.drawable.bg_test_result_correct)
            wrongAnswerBackground = ContextCompat.getDrawable(context, R.drawable.bg_test_result_wrong)
            if (view.tvAnswerA.text.equals(answer)) {
                view.sttAnswerA?.background = correctAnswerBackground
            } else if (view.tvAnswerB.text.equals(answer)) {
                view.sttAnswerB?.background = correctAnswerBackground
            } else if (view.tvAnswerC.text.equals(answer)) {
                view.sttAnswerC?.background = correctAnswerBackground
            } else if (view.tvAnswerD.text.equals(answer)) {
                view.sttAnswerD?.background = correctAnswerBackground
            }


    }
}