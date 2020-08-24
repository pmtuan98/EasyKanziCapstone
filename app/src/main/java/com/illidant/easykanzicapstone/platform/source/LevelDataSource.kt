package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.response.LevelResponse
import retrofit2.Call

interface LevelDataSource {
    interface Remote {
        fun getLevelData(): Call<LevelResponse>
    }
}