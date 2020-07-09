package com.illidant.easykanzicapstone.ui.screen.home

import com.illidant.easykanzicapstone.domain.request.LevelRequest

interface HomeContract {

    interface Presenter {
        fun listLevel(request: LevelRequest)
    }
}