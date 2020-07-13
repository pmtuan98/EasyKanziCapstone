package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.model.Lesson
import retrofit2.Call

interface LessonDataSource {
    interface Local {

    }

    interface Remote {
        fun getLessonByLevelID(id: Int): Call<List<Lesson>>
    }
}