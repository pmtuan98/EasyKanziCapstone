package com.illidant.easykanzicapstone.platform.source.remote

import com.illidant.easykanzicapstone.domain.model.Lesson
import com.illidant.easykanzicapstone.platform.api.ApiService
import com.illidant.easykanzicapstone.platform.source.LessonDataSource
import retrofit2.Call

class LessonRemoteDataSource(private val apiService: ApiService) : LessonDataSource.Remote {
    override fun getLessonByLevelID(id: Int): Call<List<Lesson>> = apiService.getLessonByLevelID(id)
}