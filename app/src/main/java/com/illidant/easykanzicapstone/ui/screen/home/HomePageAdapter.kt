package com.illidant.easykanzicapstone.ui.screen.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import java.util.*

class HomePageAdapter : RecyclerView.Adapter<HomePageAdapter.HomePageView>{
    var imageList: ArrayList<Int>? = null
    var titleList: ArrayList<String>? = null

    constructor(imageList: ArrayList<Int>?, titleList: ArrayList<String>?){
        this.imageList = imageList
        this.titleList = titleList
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HomePageView {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_homepage, viewGroup, false)
        return HomePageView(view)
    }

    override fun onBindViewHolder(homePageView: HomePageView, position: Int) {
        homePageView.imageHomePage.setImageResource(imageList!![position])
        homePageView.textTitle.setText(titleList!![position])
        homePageView.imageHomePage.setOnClickListener{
            Log.d("HPV","show ${homePageView.textTitle.text}")
        }


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