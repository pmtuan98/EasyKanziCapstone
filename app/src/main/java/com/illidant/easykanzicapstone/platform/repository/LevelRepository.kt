package com.illidant.easykanzicapstone.platform.repository

import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.domain.response.LevelResponse
import com.illidant.easykanzicapstone.platform.source.LevelDataSource
import com.illidant.easykanzicapstone.platform.source.UserDataSource
import retrofit2.Call

interface LevelRepositoryType : LevelDataSource.Remote

class LevelRepository(
    private val remote: LevelDataSource.Remote
) : LevelRepositoryType {
    override fun getLevelData(): Call<LevelResponse> = remote.getLevelData()
}