package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.model.Vocabulary
import retrofit2.Call

interface VocabularyDataSource {
    interface Remote {
        fun getVocabByKanjiID(id: Int): Call<List<Vocabulary>>

        fun getVocabByLessonID(id: Int): Call<List<Vocabulary>>
    }
}