package com.illidant.easykanzicapstone.platform.source


import com.illidant.easykanzicapstone.domain.model.Quiz
import retrofit2.Call

interface QuizDataSource {
    interface Local {

    }

    interface Remote {
        fun getQuizByLessonID(id: Int): Call<List<Quiz>>
    }
}