package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.domain.response.KanjiResponse
import retrofit2.Call

interface KanjiDataSource {
    interface Remote {
        fun getKanjiByLessonID(id: Int): Call<List<Kanji>>
        fun getKanjiByID(id: Int): Call<KanjiResponse>
        fun getVocabByKanjiID(id: Int): Call<List<Vocabulary>>
    }
}