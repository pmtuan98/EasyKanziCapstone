package com.illidant.easykanzicapstone.ui.screen.test.show_answer

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.ResultQuiz
import com.illidant.easykanzicapstone.extension.isNotEmptyAndBlank
import kotlinx.android.synthetic.main.item_test_result.view.*

class AnswerTestAdapter(
    private val listResultQuiz: List<ResultQuiz>
) : RecyclerView.Adapter<AnswerTestAdapter.AnswerView>() {

    class AnswerView(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(resultQuiz: ResultQuiz, position: Int) = with(resultQuiz) {
            itemView.apply {
                tvStt.text = (position + 1).toString()
                tvQuestion.text = quiz.question
                tvAnswerA.text = quiz.answerA
                tvAnswerB.text = quiz.answerB
                tvAnswerC.text = quiz.answerC
                tvAnswerD.text = quiz.answerD

                when (quiz.correctAnswer) {
                    quiz.answerA -> tvAnswerA.setBackgroundColor(Color.GREEN)
                    quiz.answerB -> tvAnswerB.setBackgroundColor(Color.GREEN)
                    quiz.answerC -> tvAnswerC.setBackgroundColor(Color.GREEN)
                    quiz.answerD -> tvAnswerD.setBackgroundColor(Color.GREEN)
                }

                val compare = selectedAnswer != quiz.correctAnswer
                Log.d(
                    "Result Quiz",
                    "position=${position}\n" +
                        "resultQuiz=${this@with}\n" +
                        "A:B=${quiz.answerA == quiz.answerB}\n" +
                        "B:C=${quiz.answerB == quiz.answerC}\n" +
                        "C:D=${quiz.answerC == quiz.answerD}\n" +
                        "D:A=${quiz.answerD == quiz.answerA}\n" +
                        "compare=${compare}"
                )

                if (selectedAnswer.isNotEmptyAndBlank() && selectedAnswer != quiz.correctAnswer) {
                    when (selectedAnswer) {
                        quiz.answerA -> tvAnswerA.setBackgroundColor(Color.RED)
                        quiz.answerB -> tvAnswerB.setBackgroundColor(Color.RED)
                        quiz.answerC -> tvAnswerC.setBackgroundColor(Color.RED)
                        quiz.answerD -> tvAnswerD.setBackgroundColor(Color.RED)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): AnswerTestAdapter.AnswerView {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_test_result, viewGroup, false)
        return AnswerTestAdapter.AnswerView(view)
    }

    override fun getItemCount() = listResultQuiz.size

    override fun onBindViewHolder(holder: AnswerView, position: Int) {
        holder.bindData(listResultQuiz[position], position)
    }
}