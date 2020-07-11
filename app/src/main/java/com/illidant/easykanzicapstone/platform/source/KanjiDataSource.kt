package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.model.Kanji
import retrofit2.Call

interface KanjiDataSource {
    interface Local {

    }

    interface Remote {
        fun kanjiLessonRequest(id: Int): Call<List<Kanji>>
    }
}