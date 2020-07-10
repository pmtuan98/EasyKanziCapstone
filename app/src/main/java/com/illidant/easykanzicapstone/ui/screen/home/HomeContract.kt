package com.illidant.easykanzicapstone.ui.screen.home

import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.SigninRequest

interface HomeContract {

    interface View {
        fun onDataComplete(levels: List<Level>)
    }

    interface Presenter {
        fun getLevelData()
    }
}