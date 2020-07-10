package com.illidant.easykanzicapstone.platform.repository

import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.platform.source.LevelDataSource
import retrofit2.Call

interface LevelRepositoryType : LevelDataSource.Local,LevelDataSource.Remote

class LevelRepository(
    private val remote: LevelDataSource.Remote
): LevelRepositoryType
{
    override fun getLevelData(): Call<List<Level>> = remote.getLevelData()
}