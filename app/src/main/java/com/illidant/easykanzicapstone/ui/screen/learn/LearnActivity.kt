package com.illidant.easykanzicapstone.ui.screen.learn

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import com.illidant.easykanzicapstone.platform.source.remote.VocabularyRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.flashcard.FlashcardActivity
import com.illidant.easykanzicapstone.ui.screen.learn.multiple_choice.MultipleChoiceActivity
import com.illidant.easykanzicapstone.ui.screen.learn.writing.WritingActivity
import kotlinx.android.synthetic.main.activity_learn.*
import kotlinx.android.synthetic.main.activity_learn.tvLesson
import kotlinx.android.synthetic.main.activity_learn.tvLevel

class LearnActivity : AppCompatActivity(), LearnContract.View {

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
        val lesson_id = intent.getIntExtra("LESSON_ID", 0)
        val lesson_name = intent.getStringExtra("LESSON_NAME")
        val level_name = intent.getStringExtra("LEVEL_NAME")
        tvLesson.text = lesson_name
        tvLevel.text = level_name
        presenter.vocabByLessonRequest(lesson_id)
        //Move to flashcard
        flashcardMethod.setOnClickListener({
            val intent = Intent(it.context, FlashcardActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            startActivity(intent)
        })

        writingMethod.setOnClickListener({
            val intent = Intent(it.context, WritingActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            startActivity(intent)
        })
        multipleMethod.setOnClickListener({
            val intent = Intent(it.context, MultipleChoiceActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            startActivity(intent)
        })
    }

    override fun getVocabByKanjiID(listVocab: List<Vocabulary>) {
        //Not use
    }

    override fun getVocabByLessonID(listVocab: List<Vocabulary>) {
        tvTotalVocab.text = "(${listVocab.size.toString()})"
        recyclerViewVocab!!.layoutManager = GridLayoutManager(this, 1)
        recyclerViewVocab.adapter = LearnVocabAdapter(this, listVocab)
    }

}