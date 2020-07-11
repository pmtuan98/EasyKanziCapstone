package com.illidant.easykanzicapstone.ui.screen.kanji

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView

import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Kanji
import kotlinx.android.synthetic.main.cardview_kanji.view.*

class KanjiLevelAdapter : RecyclerView.Adapter<KanjiLevelAdapter.LevelViewHolder> {

     var context: Context
     var listKanji: List<Kanji>? = null

    constructor(mContext: Context, listKanji: List<Kanji>) {
        this.context = mContext
        this.listKanji = listKanji
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_kanji, parent, false)
        return LevelViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listKanji!!.size
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.text_kanji.text =listKanji?.get(position)?.kanji
        holder.text_sio_vietnamese.text = listKanji?.get(position)?.sino_vietnamese

        //set click listener
        holder.cardView.setOnClickListener {
            // Move to detail kanji
        }

    }


    class LevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text_kanji: TextView
        var text_sio_vietnamese: TextView
        var cardView: CardView

        init {
            text_kanji = itemView.text_kanji as TextView
            text_sio_vietnamese = itemView.text_sio_vietnamese as TextView
            cardView = itemView.cardview_id as CardView
        }
    }

}