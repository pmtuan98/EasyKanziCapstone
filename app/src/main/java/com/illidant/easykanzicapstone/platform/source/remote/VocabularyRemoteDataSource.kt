package com.illidant.easykanzicapstone.platform.source.remote

import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.ApiService
import com.illidant.easykanzicapstone.platform.source.VocabularyDataSource
import retrofit2.Call

class VocabularyRemoteDataSource(private val apiService: ApiService) : VocabularyDataSource.Remote {

    override fun getVocabByKanjiID(id: Int): Call<List<Vocabulary>> =
        apiService.getVocabByKanjiID(id)

    override fun getVocabByLessonID(id: Int): Call<List<Vocabulary>> =
        apiService.getVocabByLessonID(id)

}