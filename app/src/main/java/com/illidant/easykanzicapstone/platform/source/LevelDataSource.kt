package com.illidant.easykanzicapstone.platform.source


import com.illidant.easykanzicapstone.domain.request.LevelRequest
import retrofit2.Call
import com.illidant.easykanzicapstone.domain.model.Level

interface LevelDataSource {
    interface Local {

    }

    interface Remote {
        fun listLevel(request: LevelRequest): Call<Level>
    }
}