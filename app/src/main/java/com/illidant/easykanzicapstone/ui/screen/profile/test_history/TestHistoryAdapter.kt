package com.illidant.easykanzicapstone.ui.screen.profile.test_history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.TestHistory
import com.illidant.easykanzicapstone.ui.screen.search.SearchAdapter
import kotlinx.android.synthetic.main.item_test_history.view.*

class TestHistoryAdapter : RecyclerView.Adapter<TestHistoryAdapter.TestHistoryView> {

    var listHistory: List<TestHistory>? = null
    var context: Context

    constructor(listHistory: List<TestHistory>?, context: Context) : super() {
        this.listHistory = listHistory
        this.context = context
    }


    class TestHistoryView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textLevel: TextView
        var textPoint: TextView
        var textDate: TextView

        init {
            textLevel = itemView.textTestLevel as TextView
            textPoint = itemView.textPoint as TextView
            textDate = itemView.textDateAttend as TextView
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TestHistoryView {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_test_history, viewGroup, false)
        return TestHistoryView(view)
    }

    override fun getItemCount(): Int {
        return listHistory!!.size
    }

    override fun onBindViewHolder(view: TestHistoryView, position: Int) {
        view.textLevel.text = listHistory?.get(position)?.levelName
        view.textPoint.text = listHistory?.get(position)?.resultPoint
        view.textDate.text = listHistory?.get(position)?.dateAttend
    }

}