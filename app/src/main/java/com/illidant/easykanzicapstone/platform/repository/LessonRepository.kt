package com.illidant.easykanzicapstone.platform.repository

import com.illidant.easykanzicapstone.domain.model.Lesson
import com.illidant.easykanzicapstone.platform.source.LessonDataSource
import retrofit2.Call


interface LessonRepositoryType : LessonDataSource.Remote

class LessonRepository(
    private val remote: LessonDataSource.Remote
) : LessonRepositoryType {
    override fun getLessonByLevelID(id: Int): Call<List<Lesson>> = remote.getLessonByLevelID(id)

}