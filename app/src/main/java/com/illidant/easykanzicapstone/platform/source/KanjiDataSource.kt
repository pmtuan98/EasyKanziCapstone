package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import retrofit2.Call

interface KanjiDataSource {
    interface Remote {
        fun getKanjiByLessonID(id: Int): Call<List<Kanji>>
        fun getKanjiByID(id: Int): Call<Kanji>
        fun getVocabByKanjiID(id: Int): Call<List<Vocabulary>>
    }
}