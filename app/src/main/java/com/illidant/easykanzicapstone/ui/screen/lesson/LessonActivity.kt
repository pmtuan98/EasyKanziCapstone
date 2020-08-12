package com.illidant.easykanzicapstone.ui.screen.lesson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Lesson
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.LessonRepository
import com.illidant.easykanzicapstone.platform.source.remote.LessonRemoteDataSource
import kotlinx.android.synthetic.main.activity_lesson.*


class LessonActivity : AppCompatActivity(), LessonContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        initialize()
    }
    private val lessonPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = LessonRemoteDataSource(retrofit)
        val repository = LessonRepository(remote)
        LessonPresenter(this, repository)
    }
    private fun initialize() {
        tvLevel.text = intent.getStringExtra("LEVEL_NAME")
        val levelId = intent.getIntExtra("LEVEL_ID", 0)
        lessonPresenter.lessonRequest(levelId)

    }
    override fun getLesson(listLesson: List<Lesson>) {
        recyclerViewLesson!!.layoutManager = GridLayoutManager(this, 1)
        recyclerViewLesson!!.adapter = LessonAdapter(listLesson,this)
        recyclerViewLesson.layoutManager = object : LinearLayoutManager(this) { //prevent scroll
            override fun canScrollVertically(): Boolean = false
        }
    }
}