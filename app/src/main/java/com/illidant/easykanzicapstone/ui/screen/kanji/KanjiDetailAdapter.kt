package com.illidant.easykanzicapstone.ui.screen.kanji

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import com.illidant.easykanzicapstone.ui.screen.home.HomeAdapter
import kotlinx.android.synthetic.main.cardview_kanji.view.*
import kotlinx.android.synthetic.main.item_kanji_vocabulary.view.*

class KanjiDetailAdapter : RecyclerView.Adapter<KanjiDetailAdapter.KanjiDetailView> {
    var vocabList: List<Vocabulary>? = null
    var context: Context

    constructor(vocabList: List<Vocabulary>?, context: Context) : super() {
        this.vocabList = vocabList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KanjiDetailView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kanji_vocabulary, parent, false)
        return KanjiDetailView(view)
    }


    override fun getItemCount(): Int {
        return vocabList!!.size
    }

    override fun onBindViewHolder(holder: KanjiDetailView, position: Int) {
        holder.textVocabKanji.text =vocabList?.get(position)?.kanji_vocab
        holder.textVocabHira.text =vocabList?.get(position)?.hiragana
        holder.textVocabVn.text =vocabList?.get(position)?.vocab_meaning
    }

    class KanjiDetailView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textVocabKanji: TextView
        var textVocabHira: TextView
        var textVocabVn: TextView

        init {
            textVocabKanji = itemView.textVocabKanji as TextView
            textVocabHira = itemView.textVocabHira as TextView
            textVocabVn = itemView.textVocabVn as TextView
        }
    }
}