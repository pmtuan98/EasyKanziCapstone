package com.illidant.easykanzicapstone.ui.screen.lesson

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
import com.illidant.easykanzicapstone.ui.screen.kanji.KanjiContract
import com.illidant.easykanzicapstone.ui.screen.kanji.KanjiPresenter
import com.illidant.easykanzicapstone.ui.screen.learn.LearnActivity
import com.illidant.easykanzicapstone.ui.screen.test.EntryTestActivity
import kotlinx.android.synthetic.main.activity_lesson_detail.*
import kotlinx.android.synthetic.main.bottom_navigation_bar.*

class KanjiByLessonActivity : AppCompatActivity(),
    KanjiContract.View{

    private var lessonId:Int = 0
    private var lessonName:String= ""

    private val kanjiPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val remote = KanjiRemoteDataSource(retrofit)
        val repository = KanjiRepository(remote)
        KanjiPresenter(this, repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_detail)
        initialize()
        configViews()
    }


    private fun initialize() {
        lessonId = intent.getIntExtra("LESSON_ID", 0)
        lessonName = intent.getStringExtra("LESSON_NAME")
        kanjiPresenter.kanjiByLessonRequest(lessonId)
    }
    private fun configViews(){
        tvLesson.text = lessonName
        btnBack.setOnClickListener {
            finish()
        }
        btnLearn.setOnClickListener{
            val intent = Intent(it.context, LearnActivity::class.java)
            intent.putExtra("LESSON_ID", lessonId)
            intent.putExtra("LESSON_NAME", lessonName)
            startActivity(intent)
        }
    }

    // Fill kanji into cardview
    override fun getKanjiByLesson(listKanjiLesson: List<Kanji>) {
        recyclerViewLevel.adapter = KanjiByLessonAdapter(this, listKanjiLesson)
        recyclerViewLevel.layoutManager = GridLayoutManager(this, 3)
    }

    override fun getKanjiByID(listKanjiElement: Kanji) {
        //Not use
    }

    override fun getVocabByKanjiID(listVocab: List<Vocabulary>) {
        //Not use
    }


}