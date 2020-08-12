package com.illidant.easykanzicapstone.ui.screen.ranking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.TestRanking
import kotlinx.android.synthetic.main.item_ranking.view.*

class RankingAdapter : RecyclerView.Adapter<RankingAdapter.RankingView> {

    var listRanking: List<TestRanking>? = null
    var context: Context

    constructor(listRanking: List<TestRanking>?, context: Context) : super() {
        this.listRanking = listRanking
        this.context = context
    }


    class RankingView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textUserName: TextView
        var textPoint: TextView
        var textTime: TextView

        init {
            textUserName = itemView.tvUsername as TextView
            textPoint = itemView.tvPoint as TextView
            textTime = itemView.tvTime as TextView
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RankingView {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_ranking, viewGroup, false)
        return RankingView(view)
    }

    override fun getItemCount(): Int {
        return listRanking!!.size
    }

    override fun onBindViewHolder(view: RankingView, position: Int) {
        view.textUserName.text = listRanking?.get(position)?.userName
        view.textPoint.text = listRanking?.get(position)?.resultPoint
        view.textTime.text = listRanking?.get(position)?.timeTaken
    }
}

