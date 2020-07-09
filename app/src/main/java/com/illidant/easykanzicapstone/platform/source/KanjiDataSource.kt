package com.illidant.easykanzicapstone.platform.source


interface KanjiDataSource {

    interface Local {
        fun getKanjiBasicLocal()

        fun getKanjiAdvanceLocal()

        fun getFavoriteKanjiBasic()

        fun getFavoriteKanjiAdvance()

        fun updateKanjiBasicLocal()

        fun updateKanjiAdvanceLocal()

        fun updateFavoriteKanjiBasic()

        fun updateFavoriteKanjiAdvance()

        fun updateTagKanjiBasic()

        fun updateTagKanjiAdvance()

        fun getStrokeOrder()

        fun searchKanji()
    }

    interface Remote {
        fun getKanjiBasicRemote()

        fun getKanjiAdvanceRemote()
    }
}
