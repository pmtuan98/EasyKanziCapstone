package com.illidant.easykanzicapstone.platform.repository

import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.domain.response.KanjiResponse
import com.illidant.easykanzicapstone.platform.source.KanjiDataSource
import retrofit2.Call


interface KanjiRepositoryType : KanjiDataSource.Remote
class KanjiRepository(
    private val remote: KanjiDataSource.Remote
) : KanjiRepositoryType {
    override fun getKanjiByLessonID(id: Int): Call<List<Kanji>> = remote.getKanjiByLessonID(id)
    override fun getKanjiByID(id: Int): Call<KanjiResponse> = remote.getKanjiByID(id)
    override fun getVocabByKanjiID(id: Int): Call<List<Vocabulary>> = remote.getVocabByKanjiID(id)
}