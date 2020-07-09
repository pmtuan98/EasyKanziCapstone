package com.illidant.easykanzicapstone.platform.source.remote

import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.domain.request.LevelRequest
import com.illidant.easykanzicapstone.platform.api.ApiService
import com.illidant.easykanzicapstone.platform.source.LevelDataSource
import retrofit2.Call

class LevelRemoteDataSource(private val apiService: ApiService):LevelDataSource.Remote {
    override fun listLevel(request: LevelRequest): Call<Level> = apiService.listLevel(request)
}