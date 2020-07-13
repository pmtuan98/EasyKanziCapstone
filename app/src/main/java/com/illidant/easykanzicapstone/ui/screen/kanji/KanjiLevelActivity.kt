package com.illidant.easykanzicapstone.ui.screen.kanji

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.domain.model.Lesson
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.KanjiRepository
import com.illidant.easykanzicapstone.platform.repository.LessonRepository
import com.illidant.easykanzicapstone.platform.source.local.SharedPrefs
import com.illidant.easykanzicapstone.platform.source.local.UserLocalDataSource
import com.illidant.easykanzicapstone.platform.source.remote.KanjiRemoteDataSource
import com.illidant.easykanzicapstone.platform.source.remote.LessonRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.lesson.LessonContract
import com.illidant.easykanzicapstone.ui.screen.lesson.LessonPresenter
import kotlinx.android.synthetic.main.activity_level.*
import kotlinx.android.synthetic.main.bottom_navigation_bar.*

class KanjiLevelActivity : AppCompatActivity(), KanjiContract.View, LessonContract.View {

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = KanjiRemoteDataSource(retrofit)
        val repository = KanjiRepository(remote)
        KanjiPresenter(this, repository)
    }
    private val lesson_presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = LessonRemoteDataSource(retrofit)
        val repository = LessonRepository(remote)
        LessonPresenter(this, repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)
        initialize()

    }

    companion object {
        fun getIntent(context: Context) = Intent(context, KanjiLevelActivity::class.java)
    }

    private fun initialize() {
        val level_name = intent.getStringExtra("LEVEL_NAME")
        text_level_name.text = level_name
        recycler_level.layoutManager = GridLayoutManager(this, 3)
        presenter.kanjiRequest(1)

        val level_id = intent.getIntExtra("LEVEL_ID", 0)
        lesson_presenter.lessonRequest(level_id)
    }



    override fun fillKanji(listKanjiLesson: List<Kanji>) {
        recycler_level.adapter = KanjiLevelAdapter(this, listKanjiLesson)
    }

    override fun fillLesson(listLesson: List<Lesson>) {

    }



}