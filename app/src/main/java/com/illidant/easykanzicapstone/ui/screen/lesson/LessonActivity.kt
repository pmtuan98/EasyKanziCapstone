package com.illidant.easykanzicapstone.ui.screen.lesson

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.illidant.easykanzicapstone.BaseActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Lesson
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.LessonRepository
import com.illidant.easykanzicapstone.platform.source.remote.LessonRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.test.EntryTestActivity
import kotlinx.android.synthetic.main.activity_lesson.*


class LessonActivity : BaseActivity(), LessonContract.View {
    private var levelId: Int = 0
    private var levelName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        initialize()
        configViews()
    }

    private val lessonPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = LessonRemoteDataSource(retrofit)
        val repository = LessonRepository(remote)
        LessonPresenter(this, repository)
    }

    private fun initialize() {
        levelId = intent.getIntExtra("LEVEL_ID", 0)
        levelName = intent.getStringExtra("LEVEL_NAME")
        lessonPresenter.lessonRequest(levelId)
    }

    private fun configViews() {
        tvLevel.text = levelName
        btnBack.setOnClickListener {
            finish()
        }
        btnTest.setOnClickListener {
            val intent = Intent(it.context, EntryTestActivity::class.java)
            intent.putExtra("LEVEL_NAME", levelName)
            intent.putExtra("LEVEL_ID", levelId)
            startActivity(intent)
        }
    }

    override fun getLesson(listLesson: List<Lesson>) {
        recyclerViewLesson!!.layoutManager = GridLayoutManager(this, 1)
        recyclerViewLesson!!.adapter = LessonAdapter(listLesson, this)
        recyclerViewLesson.layoutManager = object : LinearLayoutManager(this) { //prevent scroll
            override fun canScrollVertically(): Boolean = false
        }
    }
}