package com.illidant.easykanzicapstone.ui.screen.learn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import kotlinx.android.synthetic.main.cardview_learn.view.*

class LearnMethodAdapter : RecyclerView.Adapter<LearnMethodAdapter.LearnView> {
    var imageLearnMethod: List<Int>? = null
    var titleLearnMethod: List<String>? = null

    constructor(
        imageLearnMethod: MutableList<Int>?,
        titleLearnMethod: MutableList<String>?) : super() {
        this.imageLearnMethod = imageLearnMethod
        this.titleLearnMethod = titleLearnMethod
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):LearnView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_learn, parent, false)
        return LearnView(view)
    }

    override fun getItemCount(): Int {
        return imageLearnMethod!!.size

    }

    override fun onBindViewHolder(holder: LearnView, position: Int) {
        holder.imageMethod.setImageResource(imageLearnMethod?.get(position)!!)
        holder.titleMethod.setText(titleLearnMethod?.get(position))
    }
    class LearnView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageMethod: ImageView
        var titleMethod: TextView
        init {
            imageMethod = itemView.image_learn_method
            titleMethod = itemView.title_learn_method
        }
    }

}