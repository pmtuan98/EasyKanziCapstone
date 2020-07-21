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
import com.illidant.easykanzicapstone.ui.screen.learn.writing.WritingActivity
import kotlinx.android.synthetic.main.activity_learn.*
import kotlinx.android.synthetic.main.activity_learn.txt_lesson
import kotlinx.android.synthetic.main.activity_learn.txt_level

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
        txt_lesson.text = lesson_name
        txt_level.text = level_name
        presenter.vocabByLessonRequest(lesson_id)
        //Move to flashcard
        flashcard_method.setOnClickListener({
            val intent = Intent(it.context, FlashcardActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            intent.putExtra("LESSON_NAME", lesson_name)
            intent.putExtra("LEVEL_NAME", level_name)
            startActivity(intent)
        })

        writing_method.setOnClickListener({
            val intent = Intent(it.context, WritingActivity::class.java)
            intent.putExtra("LESSON_ID", lesson_id)
            intent.putExtra("LESSON_NAME", lesson_name)
            intent.putExtra("LEVEL_NAME", level_name)
            startActivity(intent)
        })
    }
    override fun getVocabByKanjiID(listVocab: List<Vocabulary>) {
     //Not use
    }

    override fun getVocabByLessonID(listVocab: List<Vocabulary>) {
        recycler_vocab!!.layoutManager = GridLayoutManager(this, 1)
        recycler_vocab.adapter = LearnVocabAdapter(this, listVocab)
    }

}