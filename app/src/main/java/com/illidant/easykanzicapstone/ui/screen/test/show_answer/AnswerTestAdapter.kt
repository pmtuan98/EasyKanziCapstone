package com.illidant.easykanzicapstone.ui.screen.test.show_answer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
                val correctAnswerBackground =
                    ContextCompat.getDrawable(context, R.drawable.bg_test_result_correct)
                val wrongAnswerBackground =
                    ContextCompat.getDrawable(context, R.drawable.bg_test_result_wrong)
                val defaultAnswerBackground =
                    ContextCompat.getDrawable(context, R.drawable.bg_test_result_answer)
                when (quiz.correctAnswer) {
                    quiz.answerA -> sttAnswerA?.background = correctAnswerBackground
                    quiz.answerB -> sttAnswerB?.background = correctAnswerBackground
                    quiz.answerC -> sttAnswerC?.background = correctAnswerBackground
                    quiz.answerD -> sttAnswerD?.background = correctAnswerBackground
                }

                if (selectedAnswer.isNotEmptyAndBlank() && selectedAnswer != quiz.correctAnswer) {
                    when (selectedAnswer) {
                        quiz.answerA -> sttAnswerA?.background = wrongAnswerBackground
                        quiz.answerB -> sttAnswerB?.background = wrongAnswerBackground
                        quiz.answerC -> sttAnswerC?.background = wrongAnswerBackground
                        quiz.answerD -> sttAnswerD?.background = wrongAnswerBackground
                    }
                }else if(!selectedAnswer.isNotEmptyAndBlank()) {
                     sttAnswerA?.background = defaultAnswerBackground
                     sttAnswerB?.background = defaultAnswerBackground
                     sttAnswerC?.background = defaultAnswerBackground
                     sttAnswerD?.background = defaultAnswerBackground
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AnswerView {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_test_result, viewGroup, false)
        return AnswerView(view)
    }

    override fun getItemCount() = listResultQuiz.size


    override fun onBindViewHolder(holder: AnswerView, position: Int) {
        holder.bindData(listResultQuiz[position], position)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}