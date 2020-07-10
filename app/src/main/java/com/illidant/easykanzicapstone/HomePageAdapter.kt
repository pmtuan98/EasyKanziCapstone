package com.illidant.easykanzicapstone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.domain.model.Level


class HomePageAdapter : RecyclerView.Adapter<HomePageAdapter.HomePageView>{
    var levelList: List<Level>? = null

    constructor(leveList: List<Level>){
        this.levelList = leveList
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HomePageAdapter.HomePageView {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_homepage, viewGroup, false)
        return HomePageView(view)
    }

    override fun onBindViewHolder(homePageView: HomePageView, position: Int) {
        homePageView.imageHomePage.setImageResource(R.drawable.jpd121)
        homePageView.textTitle.text = levelList?.get(position)?.name
        homePageView.imageHomePage.setOnClickListener {
            Toast.makeText(it.context,"Click on ${homePageView.textTitle.text }",Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return levelList!!.size
    }

    class HomePageView(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imageHomePage: ImageView
        var textTitle: TextView

        init {
            imageHomePage = itemView.findViewById<View>(R.id.image_homepage) as ImageView
            textTitle = itemView.findViewById<View>(R.id.text_title) as TextView
        }
    }
}