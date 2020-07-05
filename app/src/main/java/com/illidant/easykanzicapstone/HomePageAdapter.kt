package com.illidant.easykanzicapstone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class HomePageAdapter : RecyclerView.Adapter<HomePageAdapter.HomePageView>{
    var imageList: ArrayList<Int>? = null
    var titleList: ArrayList<String>? = null

    constructor(imageList: ArrayList<Int>?, titleList: ArrayList<String>?){
        this.imageList = imageList
        this.titleList = titleList
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HomePageAdapter.HomePageView {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_homepage, viewGroup, false)
        return HomePageView(view)
    }

    override fun onBindViewHolder(homePageView: HomePageView, position: Int) {
        homePageView.imageHomePage.setImageResource(imageList!![position])
        homePageView.textTitle.setText(titleList!![position])
    }

    override fun getItemCount(): Int {
        return imageList!!.size
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