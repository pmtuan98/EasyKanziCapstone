package com.illidant.easykanzicapstone.ui.screen.learn

import android.util.Log
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LearnPresenter(
    private val view: LearnContract.View,
    private val repository: VocabularyRepository) : LearnContract.Presenter {
    override fun vocabByLessonRequest(id: Int) {
       repository.getVocabByLessonID(id).enqueue(object : Callback<List<Vocabulary>>{

           override fun onResponse(call: Call<List<Vocabulary>>, response: Response<List<Vocabulary>>) {
               response.body()?.let { view.getVocabByLessonID(it) }
           }

           override fun onFailure(call: Call<List<Vocabulary>>, t: Throwable) {
           }
       })
    }
}