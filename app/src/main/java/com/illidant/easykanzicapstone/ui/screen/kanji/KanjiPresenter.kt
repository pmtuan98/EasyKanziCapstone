package com.illidant.easykanzicapstone.ui.screen.kanji

import android.util.Log
import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.domain.response.KanjiResponse
import com.illidant.easykanzicapstone.platform.repository.KanjiRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KanjiPresenter(
    private val view: KanjiContract.View,
    private val repository: KanjiRepository
) : KanjiContract.Presenter {
    override fun kanjiByLessonRequest(id: Int) {
        repository.getKanjiByLessonID(id).enqueue(object : Callback<List<Kanji>> {
            override fun onResponse(call: Call<List<Kanji>>, response: Response<List<Kanji>>) {
                response.body()?.let { view.getKanjiByLesson(it) }
            }

            override fun onFailure(call: Call<List<Kanji>>, t: Throwable) {

            }
        })

    }

    override fun kanjiByIDRequest(id: Int) {
        repository.getKanjiByID(id).enqueue(object : Callback<KanjiResponse> {
            override fun onFailure(call: Call<KanjiResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<KanjiResponse>, response: Response<KanjiResponse>) {
                response.body()?.let { view.getKanjiByID(it.body) }
            }
        })

        repository.getVocabByKanjiID(id).enqueue(object : Callback<List<Vocabulary>> {
            override fun onResponse(
                call: Call<List<Vocabulary>>,
                response: Response<List<Vocabulary>>
            ) {
                response.body()?.let { view.getVocabByKanjiID(it) }
            }

            override fun onFailure(call: Call<List<Vocabulary>>, t: Throwable) {
            }

        })
    }
}