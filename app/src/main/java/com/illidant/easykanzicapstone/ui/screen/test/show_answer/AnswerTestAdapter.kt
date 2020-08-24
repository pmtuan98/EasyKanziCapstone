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
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.illidant.easykanzicapstone.domain.model.Quiz
import kotlinx.android.synthetic.main.activity_multiple_choice.*
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
//         var tvStt: TextView
//         var tvQuestion: TextView
//         var tvAnswerA: TextView
//         var tvAnswerB: TextView
//         var tvAnswerC: TextView
//         var tvAnswerD: TextView
//         var sttAnswerA: TextView
//         var sttAnswerB: TextView
//         var sttAnswerC: TextView
//         var sttAnswerD: TextView
//         init {
//             tvStt = itemView.tvStt as TextView
//             tvQuestion = itemView.tvQuestion as TextView
//             tvAnswerA = itemView.tvAnswerA as TextView
//             tvAnswerB = itemView.tvAnswerB as TextView
//             tvAnswerC = itemView.tvAnswerC as TextView
//             tvAnswerD = itemView.tvAnswerD as TextView
//             sttAnswerA = itemView.sttAnswerA as TextView
//             sttAnswerB = itemView.sttAnswerB as TextView
//             sttAnswerC = itemView.sttAnswerC as TextView
//             sttAnswerD = itemView.sttAnswerD as TextView
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
//     override fun getItemId(position: Int): Long {
//         return position.toLong()
//     }

//     override fun getItemViewType(position: Int): Int {
//         return position
//     }

//     override fun onBindViewHolder(view: AnswerView, position: Int) {

//         view.tvStt.text = (position + 1).toString()
//         view.tvQuestion.text = listQuiz?.get(position)?.question
//         view.tvAnswerA.text = listQuiz?.get(position)?.answerA
//         view.tvAnswerB.text = listQuiz?.get(position)?.answerB
//         view.tvAnswerC.text = listQuiz?.get(position)?.answerC
//         view.tvAnswerD.text = listQuiz?.get(position)?.answerD

//             var answer = listQuiz?.get(position)?.correctAnswer
//             var correctAnswerBackground: Drawable?
//             var wrongAnswerBackground: Drawable?
//             correctAnswerBackground = ContextCompat.getDrawable(context, R.drawable.bg_test_result_correct)
//             wrongAnswerBackground = ContextCompat.getDrawable(context, R.drawable.bg_test_result_wrong)
//             if (view.tvAnswerA.text.equals(answer)) {
//                 view.sttAnswerA?.background = correctAnswerBackground
//             } else if (view.tvAnswerB.text.equals(answer)) {
//                 view.sttAnswerB?.background = correctAnswerBackground
//             } else if (view.tvAnswerC.text.equals(answer)) {
//                 view.sttAnswerC?.background = correctAnswerBackground
//             } else if (view.tvAnswerD.text.equals(answer)) {
//                 view.sttAnswerD?.background = correctAnswerBackground
//             }
    }
}