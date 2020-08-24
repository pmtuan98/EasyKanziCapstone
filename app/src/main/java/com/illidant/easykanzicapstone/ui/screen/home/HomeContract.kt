package com.illidant.easykanzicapstone.ui.screen.home

import com.illidant.easykanzicapstone.domain.model.Level

interface HomeContract {

    interface View {
        fun onDataComplete(levels: List<Level>)
    }

    interface Presenter {
        fun getLevelData()
    }
}