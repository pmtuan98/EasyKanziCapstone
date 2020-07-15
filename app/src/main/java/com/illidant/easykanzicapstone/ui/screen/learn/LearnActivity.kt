package com.illidant.easykanzicapstone.ui.screen.learn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.KanjiRepository
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import com.illidant.easykanzicapstone.platform.source.remote.VocabularyRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.kanji.KanjiPresenter
import kotlinx.android.synthetic.main.activity_learn.*

class LearnActivity : AppCompatActivity(), LearnContract.View {
    var imageLearnMethod: MutableList<Int> = mutableListOf()
    var titleLearnMethod: MutableList<String> = mutableListOf()

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = VocabularyRemoteDataSource(retrofit)
        val repository = VocabularyRepository(remote)
        LearnPresenter(this, repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)
        initialize()
    }
    private fun initialize() {
        imageLearnMethod.add(R.drawable.icon_flashcard_learn)
        imageLearnMethod.add(R.drawable.icon_writing_learn)
        imageLearnMethod.add(R.drawable.icon_multiple_choice_learn)
        titleLearnMethod?.add("Flashcard")
        titleLearnMethod?.add("Writing")
        titleLearnMethod?.add("Multiple choice")
        recycler_learn!!.layoutManager = GridLayoutManager(this, 3)
        recycler_learn!!.adapter = LearnMethodAdapter(imageLearnMethod, titleLearnMethod)


        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        text_learn_lesson.text = intent.getStringExtra("LESSON_NAME")
        text_learn_level.text =  intent.getStringExtra("LEVEL_NAME")
        presenter.vocabByLessonRequest(lesson_id)
    }

    override fun getVocabByKanjiID(listVocab: List<Vocabulary>) {
     //Not use
    }

    override fun getVocabByLessonID(listVocab: List<Vocabulary>) {
        recycler_vocab!!.layoutManager = GridLayoutManager(this, 1)
        recycler_vocab.adapter = LearnVocabAdapter(this, listVocab)
    }

}