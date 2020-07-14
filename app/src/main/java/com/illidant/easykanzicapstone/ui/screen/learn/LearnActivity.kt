package com.illidant.easykanzicapstone.ui.screen.learn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.illidant.easykanzicapstone.R
import kotlinx.android.synthetic.main.activity_learn.*

class LearnActivity : AppCompatActivity() {
    var imageLearnMethod: MutableList<Int> = mutableListOf()
    var titleLearnMethod: MutableList<String> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)
        initialize()
    }
    private fun initialize() {
        recycler_learn!!.layoutManager = GridLayoutManager(this, 3)
        imageLearnMethod.add(R.drawable.icon_flashcard_learn)
        imageLearnMethod.add(R.drawable.icon_writing_learn)
        imageLearnMethod.add(R.drawable.icon_multiple_choice_learn)

        titleLearnMethod?.add("Flashcard")
        titleLearnMethod?.add("Writing")
        titleLearnMethod?.add("Multiple choice")

        recycler_learn!!.adapter = LearnMethodAdapter(imageLearnMethod, titleLearnMethod)
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, LearnActivity::class.java)
    }
}