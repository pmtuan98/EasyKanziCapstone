package com.illidant.easykanzicapstone.ui.screen.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.ui.screen.kanji.KanjiLevelActivity
import kotlinx.android.synthetic.main.row_homepage.view.*


class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomePageView>{
    var levelList: List<Level>? = null
    var context: Context

    constructor(context: Context,leveList: List<Level>){
        this.levelList = leveList
        this.context = context
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HomePageView {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_homepage, viewGroup, false)
        return HomePageView(view)
    }

    override fun onBindViewHolder(homePageView: HomePageView, position: Int) {
        homePageView.imageHomePage.setImageResource(R.drawable.jpd121)
        homePageView.textTitle.text = levelList?.get(position)?.name
        val level_name = levelList?.get(position)?.name
        homePageView.imageHomePage.setOnClickListener {
            //Send level id to kanji level activity
            val intent = Intent(it.context, KanjiLevelActivity::class.java)
            intent.putExtra("LEVEL_NAME", level_name)
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return levelList!!.size
    }

    class HomePageView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageHomePage: ImageView
        var textTitle: TextView

        init {
            imageHomePage = itemView.image_homepage as ImageView
            textTitle = itemView.text_title as TextView
        }
    }
}