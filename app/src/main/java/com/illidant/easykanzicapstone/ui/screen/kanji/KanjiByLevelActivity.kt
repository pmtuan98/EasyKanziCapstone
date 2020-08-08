package com.illidant.easykanzicapstone.ui.screen.kanji

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.domain.model.Lesson
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.KanjiRepository
import com.illidant.easykanzicapstone.platform.repository.LessonRepository
import com.illidant.easykanzicapstone.platform.source.remote.KanjiRemoteDataSource
import com.illidant.easykanzicapstone.platform.source.remote.LessonRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.learn.LearnActivity
import com.illidant.easykanzicapstone.ui.screen.lesson.LessonContract
import com.illidant.easykanzicapstone.ui.screen.lesson.LessonPresenter
import com.illidant.easykanzicapstone.ui.screen.test.EntryTestActivity
import kotlinx.android.synthetic.main.activity_level.*
import kotlinx.android.synthetic.main.bottom_navigation_bar.*

class KanjiByLevelActivity : AppCompatActivity(), KanjiContract.View, LessonContract.View {

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


    private fun initialize() {
        tvLevel.text = intent.getStringExtra("LEVEL_NAME")
        val level_id = intent.getIntExtra("LEVEL_ID", 0)
        recyclerViewLevel.layoutManager = GridLayoutManager(this, 3)
        lesson_presenter.lessonRequest(level_id)

        btnTest.setOnClickListener {
            val intent = Intent(it.context, EntryTestActivity::class.java)
            intent.putExtra("LEVEL_NAME", tvLevel.text as String?)
            intent.putExtra("LEVEL_ID", level_id)
            startActivity(intent)
        }
    }
    // Fill kanji into cardview
    override fun getKanjiByLesson(listKanjiLesson: List<Kanji>) {
        recyclerViewLevel.adapter = KanjiByLevelAdapter(this, listKanjiLesson)
    }

    override fun getKanjiByID(listKanjiElement: Kanji) {
      //Not use
    }

    override fun getVocabByKanjiID(listVocab: List<Vocabulary>) {
       //Not use
    }

    override fun getLesson(listLesson: List<Lesson>) {

        var lesson_names = mutableListOf<String>()
        var lesson_ids = mutableListOf<Int>()
        for(lesson in listLesson) {
            lesson_names.add(lesson.name)
            lesson_ids.add(lesson.id)
        }

        fun checkNextPreviousButton(position: Int) {
            if(position <= 0) {
                btnLessonBack.visibility = View.INVISIBLE

            }else {
                btnLessonBack.visibility = View.VISIBLE
            }
            if(position + 1 >= lesson_ids.size){
                btnLessonNext.visibility = View.INVISIBLE
            }else {
                btnLessonNext.visibility = View.VISIBLE
            }
        }

        lessonSpinner.adapter = ArrayAdapter<String>(this,R.layout.item_lesson_spinner, lesson_names)

        lessonSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(applicationContext,"Please choose a lesson", Toast.LENGTH_SHORT).show()
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    var lesson_id = lesson_ids.get(p2)
                    presenter.kanjiByLessonRequest(lesson_id)
                    checkNextPreviousButton(p2)
                    btnLearn.setOnClickListener{
                        val intent = Intent(it.context, LearnActivity::class.java)
                        var lesson_position = lessonSpinner.selectedItemPosition
                        intent.putExtra("LESSON_ID", lesson_id)
                        intent.putExtra("LESSON_NAME", lesson_names[lesson_position])
                        intent.putExtra("LEVEL_NAME", tvLevel.text)
                        startActivity(intent)
                    }
            }

        }

        btnLessonBack.setOnClickListener{
            var lesson_position = lessonSpinner.selectedItemPosition
            if(lesson_position > 0) {
                lesson_position = lesson_position -1
                lessonSpinner.setSelection(lesson_position)
                checkNextPreviousButton(lesson_position)
            }
        }

        btnLessonNext.setOnClickListener{
            var lesson_position = lessonSpinner.selectedItemPosition
            if(lesson_position + 1 <  lesson_ids.size) {
                lesson_position = lesson_position + 1
                lessonSpinner.setSelection(lesson_position)
                checkNextPreviousButton(lesson_position)
            }
        }

    }



}