package com.illidant.easykanzicapstone.platform.repository

import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.platform.source.KanjiDataSource
import retrofit2.Call


interface KanjiRepositoryType : KanjiDataSource.Local, KanjiDataSource.Remote

class KanjiRepository (
    private val remote: KanjiDataSource.Remote
): KanjiRepositoryType{
    override fun kanjiLessonRequest(id: Int): Call<List<Kanji>> = remote.kanjiLessonRequest(id)
}