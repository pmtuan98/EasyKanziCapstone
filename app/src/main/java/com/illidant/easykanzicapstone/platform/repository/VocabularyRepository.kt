package com.illidant.easykanzicapstone.platform.repository

import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.source.KanjiDataSource
import com.illidant.easykanzicapstone.platform.source.VocabularyDataSource
import retrofit2.Call

interface VocabularyRepositoryType : VocabularyDataSource.Local, VocabularyDataSource.Remote

class VocabularyRepository(
    private val remote: VocabularyDataSource.Remote
) : VocabularyRepositoryType {
    override fun getVocabByKanjiID(id: Int): Call<List<Vocabulary>> = remote.getVocabByKanjiID(id)

    override fun getVocabByLessonID(id: Int): Call<List<Vocabulary>> = remote.getVocabByLessonID(id)

}