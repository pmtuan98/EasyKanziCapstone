package com.illidant.easykanzicapstone.ui.screen.learn

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.illidant.easykanzicapstone.BaseActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import com.illidant.easykanzicapstone.platform.source.remote.VocabularyRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.flashcard.FlashcardActivity
import com.illidant.easykanzicapstone.ui.screen.learn.multiple_choice.MultipleChoiceActivity
import com.illidant.easykanzicapstone.ui.screen.learn.writing.WritingActivity
import kotlinx.android.synthetic.main.activity_learn.*

class LearnActivity : BaseActivity(), LearnContract.View {

    private var lessonId: Int = 0
    private var lessonName: String = ""
    
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
        configViews()
    }

    private fun initialize() {
        lessonId = intent.getIntExtra("LESSON_ID", 0)
        lessonName = intent.getStringExtra("LESSON_NAME")
        presenter.vocabByLessonRequest(lessonId)
    }

    private fun configViews() {
        tvLesson.text = lessonName
        //Move to flashcard
        flashcardMethod.setOnClickListener({
            val intent = Intent(it.context, FlashcardActivity::class.java)
            intent.putExtra("LESSON_ID", lessonId)
            startActivity(intent)
        })

        writingMethod.setOnClickListener({
            val intent = Intent(it.context, WritingActivity::class.java)
            intent.putExtra("LESSON_ID", lessonId)
            startActivity(intent)
        })
        multipleMethod.setOnClickListener({
            val intent = Intent(it.context, MultipleChoiceActivity::class.java)
            intent.putExtra("LESSON_ID", lessonId)
            startActivity(intent)
        })

        btnBack.setOnClickListener {
            finish()
        }
    }

    override fun getVocabByKanjiID(listVocab: List<Vocabulary>) {
        //Not use
    }

    override fun getVocabByLessonID(listVocab: List<Vocabulary>) {
        tvTotalVocab.text = "(${listVocab.size})"
        recyclerViewVocab!!.layoutManager = GridLayoutManager(this, 1)
        recyclerViewVocab.adapter = LearnVocabAdapter(this, listVocab)
    }

}