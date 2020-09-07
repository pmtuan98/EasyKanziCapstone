package com.illidant.easykanzicapstone.ui.screen.learn

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.illidant.easykanzicapstone.BaseActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Quiz
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.QuizRepository
import com.illidant.easykanzicapstone.platform.repository.VocabularyRepository
import com.illidant.easykanzicapstone.platform.source.remote.QuizRemoteDataSource
import com.illidant.easykanzicapstone.platform.source.remote.VocabularyRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.flashcard.FlashcardActivity
import com.illidant.easykanzicapstone.ui.screen.learn.multiple_choice.MultipleChoiceActivity
import com.illidant.easykanzicapstone.ui.screen.learn.writing.WritingActivity
import com.illidant.easykanzicapstone.ui.screen.quiz.QuizContract
import com.illidant.easykanzicapstone.ui.screen.quiz.QuizPresenter
import kotlinx.android.synthetic.main.activity_learn.*

class LearnActivity : BaseActivity(), LearnContract.View, QuizContract.View {

    private var lessonId: Int = 0
    private var lessonName: String = ""
    private val mutualListQuiz: MutableList<Quiz> = mutableListOf()

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = VocabularyRemoteDataSource(retrofit)
        val repository = VocabularyRepository(remote)
        LearnPresenter(this, repository)
    }

    private val quizPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = QuizRemoteDataSource(retrofit)
        val repository = QuizRepository(remote)
        QuizPresenter(this, repository)
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
        quizPresenter.quizByLessonRequest(lessonId)
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
            if(mutualListQuiz.isEmpty()){
                val errDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                errDialog.contentText = "No quiz data."
                errDialog.show()
            }else{
                val intent = Intent(it.context, MultipleChoiceActivity::class.java)
                intent.putExtra("LESSON_ID", lessonId)
                startActivity(intent)
            }
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

    override fun getQuizByLessonID(listQuiz: List<Quiz>) {
        mutualListQuiz.addAll(listQuiz)
    }

    override fun getQuizByLevelID(listQuiz: List<Quiz>) {
        //Not use
    }

}