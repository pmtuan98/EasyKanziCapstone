package com.illidant.easykanzicapstone.ui.screen.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.ui.screen.kanji.KanjiByLevelActivity
import kotlinx.android.synthetic.main.item_home.view.*


class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomePageView>{
    var levelList: List<Level>? = null
    var context: Context

    constructor(context: Context,leveList: List<Level>){
        this.levelList = leveList
        this.context = context
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HomePageView {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_home, viewGroup, false)
        return HomePageView(view)
    }

    override fun onBindViewHolder(homePageView: HomePageView, position: Int) {
        homePageView.imgLevel.setImageResource(R.drawable.jpd111)
        homePageView.tvLevelName.text = levelList?.get(position)?.name
        homePageView.tvLevelDes.text = levelList?.get(position)?.description
        homePageView.itemView.setOnClickListener {
            val level_name = levelList?.get(position)?.name
            val level_id = levelList?.get(position)?.id
            val intent = Intent(it.context, KanjiByLevelActivity::class.java)
            intent.putExtra("LEVEL_NAME", level_name)
            intent.putExtra("LEVEL_ID", level_id)
            context.startActivity(intent)


        }
    }

    override fun getItemCount(): Int {
        return levelList!!.size
    }

    class HomePageView(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imgLevel: ImageView
    var tvLevelName: TextView
    var tvLevelDes: TextView
    init {
        imgLevel = itemView.image_homepage as ImageView
        tvLevelName = itemView.tvTitleLevel as TextView
        tvLevelDes = itemView.tvLevelDescription as TextView
    }
}
}