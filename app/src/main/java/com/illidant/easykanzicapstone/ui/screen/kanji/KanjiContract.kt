package com.illidant.easykanzicapstone.ui.screen.kanji

import com.illidant.easykanzicapstone.domain.model.Kanji

interface KanjiContract{
    interface View{
        fun getKanjiByLesson(listKanjiLesson: List<Kanji>)

        fun getKanjiByID(kanji: Kanji)
    }

    interface Presenter{
        fun kanjiByLessonRequest(id: Int)

        fun kanjiByIDRequest(id: Int)
    }
}