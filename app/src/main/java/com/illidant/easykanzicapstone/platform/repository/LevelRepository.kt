package com.illidant.easykanzicapstone.platform.repository


import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.domain.request.LevelRequest
import com.illidant.easykanzicapstone.platform.source.LevelDataSource
import retrofit2.Call

interface LevelRepositoryType : LevelDataSource.Remote

class LevelRepository(
    private val remote: LevelDataSource.Remote
) : LevelRepositoryType {
    override fun listLevel(request: LevelRequest): Call<Level> = remote.listLevel(request)
}
