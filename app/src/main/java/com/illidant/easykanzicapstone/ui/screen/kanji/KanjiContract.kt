package com.illidant.easykanzicapstone.ui.screen.kanji

import com.illidant.easykanzicapstone.domain.model.Kanji

interface KanjiContract{
    interface View{
        fun getKanjiByLesson(listKanjiLesson: List<Kanji>)
    }

    interface Presenter{
        fun kanjiRequest(id: Int)
    }
}