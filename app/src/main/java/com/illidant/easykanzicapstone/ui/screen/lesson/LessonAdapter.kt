package com.illidant.easykanzicapstone.ui.screen.lesson

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Lesson
import com.illidant.easykanzicapstone.ui.screen.home.HomeActivity
import kotlinx.android.synthetic.main.item_lesson.view.*


class LessonAdapter : RecyclerView.Adapter<LessonAdapter.LessonView> {
    var lessonList: List<Lesson>? = null
    var context: Context

    constructor(levelList: List<Lesson>?, context: Context) : super() {
        this.lessonList = levelList
        this.context = context
    }

    class LessonView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvLesson: TextView

        init {
            tvLesson = itemView.tvLesson as TextView
        }
    }


    override fun getItemCount(): Int {
        return lessonList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lesson, parent, false)
        return LessonView(view)
    }

    override fun onBindViewHolder(holder: LessonView, position: Int) {
        holder.tvLesson.text = lessonList?.get(position)?.name
        val lessonId = lessonList?.get(position)?.id
        val lessonName = lessonList?.get(position)?.name
        //Click to each lesson
        holder.itemView.setOnClickListener {
            val intent = Intent(context, KanjiByLessonActivity::class.java)
            intent.putExtra("LESSON_NAME", lessonName)
            intent.putExtra("LESSON_ID", lessonId)
            context.startActivity(intent)
        }
    }
}