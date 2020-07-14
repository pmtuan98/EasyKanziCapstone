package com.illidant.easykanzicapstone.ui.screen.kanji

import android.util.Log
import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.platform.repository.KanjiRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KanjiPresenter(
    private val view: KanjiContract.View,
    private val repository: KanjiRepository): KanjiContract.Presenter {
    override fun kanjiRequest(id: Int) {
        repository.getKanjiByLessonID(id).enqueue(object : Callback<List<Kanji>>{
            override fun onResponse(call: Call<List<Kanji>>, response: Response<List<Kanji>>) {
               response.body()?.let { view.fillKanji(it) }
            }

            override fun onFailure(call: Call<List<Kanji>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}