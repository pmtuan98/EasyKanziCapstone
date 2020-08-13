package com.illidant.easykanzicapstone.platform.source.remote

import com.illidant.easykanzicapstone.domain.model.Quiz
import com.illidant.easykanzicapstone.platform.api.ApiService
import com.illidant.easykanzicapstone.platform.source.QuizDataSource
import retrofit2.Call

class QuizRemoteDataSource(
    private val apiService: ApiService
) : QuizDataSource.Remote {
    override fun getQuizByLessonID(id: Int): Call<List<Quiz>> = apiService.getQuizByLessonID(id)
    override fun getQuizByLevelID(id: Int): Call<List<Quiz>> = apiService.getQuizByLevelID(id)
}