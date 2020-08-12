package com.illidant.easykanzicapstone.ui.screen.learn.writing

import android.util.Log
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WritingPresenter(
    private val view: WritingContract.View,
    private val repository: VocabularyRepository
) : WritingContract.Presenter {

    override fun vocabByLessonRequest(id: Int) {
        repository.getVocabByLessonID(id).enqueue(object : Callback<List<Vocabulary>> {

            override fun onFailure(call: Call<List<Vocabulary>>, t: Throwable) {
                view.onError(t)
            }

            override fun onResponse(call: Call<List<Vocabulary>>, response: Response<List<Vocabulary>>) {
                response.body()?.let { view.onSuccess(it) }
            }
        })
    }

    override fun checkAnswer(correctAnswer: String, userAnswer: String) {
        if (userAnswer.equals(correctAnswer, true)) {
            view.onCorrectAnswer()
        } else {
            view.onWrongAnswer(correctAnswer, userAnswer)
        }
    }
}
