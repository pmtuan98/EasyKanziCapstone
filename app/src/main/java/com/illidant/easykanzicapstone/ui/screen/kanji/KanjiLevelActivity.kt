package com.illidant.easykanzicapstone.ui.screen.kanji

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.domain.model.Lesson
import com.illidant.easykanzicapstone.extension.toast
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
        val level_id = intent.getIntExtra("LEVEL_ID", 0)
        text_level_name.text = level_name
        recycler_level.layoutManager = GridLayoutManager(this, 3)


        lesson_presenter.lessonRequest(level_id)

    }

    // Fill kanji into cardview
    override fun fillKanji(listKanjiLesson: List<Kanji>) {
        recycler_level.adapter = KanjiLevelAdapter(this, listKanjiLesson)
    }

    override fun fillLesson(listLesson: List<Lesson>) {
        val lesson_names = mutableListOf<String>()
        val lesson_ids = mutableListOf<Int>()
        for(lesson in listLesson) {
            lesson_names.add(lesson.name)
            lesson_ids.add(lesson.id)
        }
        lesson_spinner.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, lesson_names)

        lesson_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(applicationContext,"Please choose a lesson", Toast.LENGTH_SHORT).show()
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val lesson_id = lesson_ids.get(p2)
                    presenter.kanjiRequest(lesson_id)
            }

        }
        buttonLessonPrevious.setOnClickListener{
            var lesson_position = lesson_spinner.selectedItemPosition
            if(lesson_position > 0) {
                lesson_position = lesson_position -1
                lesson_spinner.setSelection(lesson_position)
            }else {
                Toast.makeText(this,"Can not back",Toast.LENGTH_SHORT).show()
            }
        }

        buttonLessonNext.setOnClickListener{
            var lesson_position = lesson_spinner.selectedItemPosition
            if(lesson_position + 1 <  lesson_ids.size) {
                lesson_position = lesson_position + 1
                lesson_spinner.setSelection(lesson_position)
            }else {
                Toast.makeText(this,"Can not next",Toast.LENGTH_SHORT).show()
            }
        }


    }



}