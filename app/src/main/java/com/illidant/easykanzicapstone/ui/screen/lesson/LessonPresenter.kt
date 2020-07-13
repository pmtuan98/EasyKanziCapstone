package com.illidant.easykanzicapstone.ui.screen.lesson

import android.util.Log
import com.illidant.easykanzicapstone.domain.model.Lesson
import com.illidant.easykanzicapstone.platform.repository.LessonRepository
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class LessonPresenter (
    private val view : LessonContract.View,
    private val repository: LessonRepository) : LessonContract.Presenter {
    override fun lessonRequest(id: Int) {
        repository.getLessonByLevelID(id).enqueue(object : Callback<List<Lesson>>{
            override fun onResponse(call: Call<List<Lesson>>, response: Response<List<Lesson>>) {
                response.body()?.let { view.fillLesson(it) }
            }

            override fun onFailure(call: Call<List<Lesson>>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })
    }
}