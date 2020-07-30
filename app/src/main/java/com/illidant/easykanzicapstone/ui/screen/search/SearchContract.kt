package com.illidant.easykanzicapstone.ui.screen.search

import com.illidant.easykanzicapstone.domain.model.KanjiES
import com.illidant.easykanzicapstone.domain.request.SearchRequest

interface SearchContract {
    interface View {
        fun onSearchResult(listSearch: List<KanjiES>)
    }

    interface Presenter {
        fun searchKanji(request: SearchRequest)
    }
}