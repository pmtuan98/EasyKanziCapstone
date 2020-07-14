package com.illidant.easykanzicapstone.ui.screen.kanji

import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.domain.model.Vocabulary

interface KanjiContract{
    interface View{
        fun getKanjiByLesson(listKanjiLesson: List<Kanji>)

        fun getKanjiByID(kanji: Kanji)

        fun getVocabByKanjiID(listVocab: List<Vocabulary>)
    }

    interface Presenter{
        fun kanjiByLessonRequest(id: Int)

        fun kanjiByIDRequest(id: Int)
    }
}