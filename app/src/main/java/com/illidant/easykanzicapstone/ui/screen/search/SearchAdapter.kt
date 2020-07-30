package com.illidant.easykanzicapstone.ui.screen.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.KanjiES
import com.illidant.easykanzicapstone.ui.screen.kanji.KanjiDetailActivity
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchView>{
    var listSearchResult: List<KanjiES>? = null
    var context: Context

    constructor(listSearchResult: List<KanjiES>?, context: Context) : super() {
        this.listSearchResult = listSearchResult
        this.context = context
    }

    class SearchView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textKanji: TextView
        var textKanjiChinaMean: TextView
        var textLevel: TextView

        init {
            textKanji = itemView.textKanjiWord as TextView
            textKanjiChinaMean = itemView.textKanjiChinaMean as TextView
            textLevel = itemView.textKanjiLevel as TextView
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SearchView {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_search, viewGroup, false)
        return SearchView(view)
    }

    override fun getItemCount(): Int {
        return listSearchResult!!.size
    }

    override fun onBindViewHolder(view: SearchView, position: Int) {
        view.textKanji.text = listSearchResult?.get(position)?.kanji
        view.textKanjiChinaMean.text = listSearchResult?.get(position)?.sinoVietnamese
        view.textLevel.text = listSearchResult?.get(position)?.level
        var kanjiID = listSearchResult?.get(position)?.id
        view.itemView.setOnClickListener {
            val intent = Intent(it.context, KanjiDetailActivity::class.java)
            intent.putExtra("KANJI_ID", kanjiID)
            context.startActivity(intent)
        }
    }


}