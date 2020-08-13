package com.illidant.easykanzicapstone.platform.repository


import com.illidant.easykanzicapstone.domain.model.Quiz
import com.illidant.easykanzicapstone.platform.source.QuizDataSource
import retrofit2.Call

interface QuizRepositoryType : QuizDataSource.Remote
class QuizRepository(
    private val remote: QuizDataSource.Remote
) : QuizRepositoryType {
    override fun getQuizByLessonID(id: Int): Call<List<Quiz>> = remote.getQuizByLessonID(id)
    override fun getQuizByLevelID(id: Int): Call<List<Quiz>> = remote.getQuizByLevelID(id)

}