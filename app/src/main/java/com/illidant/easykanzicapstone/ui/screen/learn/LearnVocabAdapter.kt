package com.illidant.easykanzicapstone.ui.screen.learn

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.Vocabulary
import kotlinx.android.synthetic.main.item_learn_word.view.*
import java.util.*

class LearnVocabAdapter : RecyclerView.Adapter<LearnVocabAdapter.VocabView> {
    private var context: Context
    private var listVocab: List<Vocabulary>? = null
    private lateinit var textHiragana: String
    private lateinit var mTTS: TextToSpeech
    constructor(context: Context, listVocab: List<Vocabulary>?) : super() {
        this.context = context
        this.listVocab = listVocab
    }

    private fun speak() {
        mTTS.speak(textHiragana, TextToSpeech.QUEUE_FLUSH, null,null)
    }
    private fun setUpSpeaker() {
        mTTS = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                //if there is no error then set language
                mTTS.language = Locale.JAPANESE
            }
        })
        // set pitch
        mTTS.setPitch(1f)
        // set speed of speak
        mTTS.setSpeechRate(1f)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabView {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_learn_word, parent, false)
        return VocabView(view)
    }

    override fun getItemCount(): Int {
        return listVocab!!.size
    }

    override fun onBindViewHolder(holder: VocabView, position: Int) {
        setUpSpeaker()
        holder.kanji.text = listVocab?.get(position)?.kanji_vocab
        holder.hira.text = listVocab?.get(position)?.hiragana
        holder.meaning.text = listVocab?.get(position)?.vocab_meaning
        holder.itemView.setOnClickListener {//click each item to speak
            textHiragana = listVocab?.get(position)?.hiragana!! //Setup for speaker
            speak()
        }
    }

    class VocabView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var kanji: TextView
        var hira: TextView
        var meaning: TextView
        init {
            kanji = itemView.tvKanji
            hira = itemView.tvHira
            meaning = itemView.tvVietnamese
        }
    }
}