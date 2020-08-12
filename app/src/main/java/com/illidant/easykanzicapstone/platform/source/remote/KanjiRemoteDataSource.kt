package com.illidant.easykanzicapstone.platform.source.remote

import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.ApiService
import com.illidant.easykanzicapstone.platform.source.KanjiDataSource
import retrofit2.Call

class KanjiRemoteDataSource(
    private val apiService: ApiService
) : KanjiDataSource.Remote {
    override fun getKanjiByLessonID(id: Int): Call<List<Kanji>> = apiService.getKanjiByLessonID(id)
    override fun getKanjiByID(id: Int): Call<Kanji> = apiService.getKanjiByID(id)
    override fun getVocabByKanjiID(id: Int): Call<List<Vocabulary>> =
        apiService.getVocabByKanjiID(id)
}