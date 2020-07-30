package com.illidant.easykanzicapstone.ui.screen.quiz

import android.util.Log
import com.illidant.easykanzicapstone.domain.model.Quiz
import com.illidant.easykanzicapstone.platform.repository.QuizRepository
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizPresenter(
    private val view: QuizContract.View,
    private val repository: QuizRepository): QuizContract.Presenter {
    override fun quizByLessonRequest(id: Int) {

        repository.getQuizByLessonID(id).enqueue(object : Callback<List<Quiz>>{

            override fun onResponse(call: Call<List<Quiz>>, response: Response<List<Quiz>>) {
                response.body()?.let { view.getQuizByLessonID(it) }
            }

            override fun onFailure(call: Call<List<Quiz>>, t: Throwable) {
                //Nothing
            }

        })
    }

    override fun quizByLevelRequest(id: Int) {
       repository.getQuizByLevelID(id).enqueue(object : Callback<List<Quiz>>{
           override fun onFailure(call: Call<List<Quiz>>, t: Throwable) {
             //Nothing
           }

           override fun onResponse(call: Call<List<Quiz>>, response: Response<List<Quiz>>) {
              response.body()?.let { view.getQuizByLevelID(it) }
           }

       })
    }
}