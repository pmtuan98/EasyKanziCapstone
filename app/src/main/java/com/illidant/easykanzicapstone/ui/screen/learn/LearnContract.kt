package com.illidant.easykanzicapstone.ui.screen.learn

import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.domain.model.Vocabulary

interface LearnContract {
    interface View{
        fun getVocabByKanjiID(listVocab: List<Vocabulary>)

        fun getVocabByLessonID(listVocab: List<Vocabulary>)
    }

    interface Presenter{
        fun vocabByLessonRequest(id: Int)
        fun vocabByKanjiIDRequest(id : Int)
    }

}