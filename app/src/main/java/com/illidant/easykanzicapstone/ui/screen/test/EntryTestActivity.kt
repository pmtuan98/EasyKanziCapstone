package com.illidant.easykanzicapstone.ui.screen.test

import android.content.Intent
import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.illidant.easykanzicapstone.BaseActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Quiz
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.QuizRepository
import com.illidant.easykanzicapstone.platform.source.remote.QuizRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.quiz.QuizContract
import com.illidant.easykanzicapstone.ui.screen.quiz.QuizPresenter
import kotlinx.android.synthetic.main.entry_test_layout.*

class EntryTestActivity : BaseActivity(), QuizContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.entry_test_layout)
        initialize()
        configViews()
    }
    private var levelName:String = ""
    private var levelId:Int = 0
    private val mutualListQuiz: MutableList<Quiz> = mutableListOf()

    private val quizPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = QuizRemoteDataSource(retrofit)
        val repository = QuizRepository(remote)
        QuizPresenter(this, repository)
    }


    private fun initialize() {
        levelName = intent.getStringExtra("LEVEL_NAME")
        levelId = intent.getIntExtra("LEVEL_ID", 0)
        tvTestLevel.text = levelName
        quizPresenter.quizByLevelRequest(levelId)

    }
    private fun configViews() {
        btnStart.setOnClickListener {
            if(mutualListQuiz.isEmpty()){
                val errDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                errDialog.contentText = "No quiz data. Can't take the test"
                errDialog.show()
            }else {
                val intent = Intent(it.context, TestActivity::class.java)
                intent.putExtra("LEVEL_ID", levelId)
                intent.putExtra("LEVEL_NAME", levelName)
                startActivity(intent)
                finish()
            }
        }
        btnBack.setOnClickListener {
            finish()
        }
    }
    override fun getQuizByLevelID(listQuiz: List<Quiz>) {
        mutualListQuiz.addAll(listQuiz)
    }

    override fun getQuizByLessonID(listQuiz: List<Quiz>) {
        //Not uese
    }
}