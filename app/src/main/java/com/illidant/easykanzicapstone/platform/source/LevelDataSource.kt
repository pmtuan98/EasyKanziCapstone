package com.illidant.easykanzicapstone.platform.source

import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.SigninRequest
import com.illidant.easykanzicapstone.domain.request.SignupRequest
import com.illidant.easykanzicapstone.domain.response.LevelResponse
import com.illidant.easykanzicapstone.domain.response.SignupResponse
import retrofit2.Call

interface LevelDataSource {
    interface Remote {
        fun getLevelData():  Call<LevelResponse>
    }
}