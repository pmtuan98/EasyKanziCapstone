package com.illidant.easykanzicapstone.ui.screen.profile.test_history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.TestHistory
import kotlinx.android.synthetic.main.activity_test_result.*
import kotlinx.android.synthetic.main.item_test_history.view.*

class TestHistoryAdapter : RecyclerView.Adapter<TestHistoryAdapter.TestHistoryView> {

    var listHistory: List<TestHistory>? = null
    var context: Context

    constructor(listHistory: List<TestHistory>?, context: Context) : super() {
        this.listHistory = listHistory
        this.context = context
    }


    class TestHistoryView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textTime: TextView
        var textPoint: TextView
        var textDate: TextView
        var progressBar: ProgressBar
        init {
            textTime = itemView.tvTime as TextView
            textPoint = itemView.tvPoint as TextView
            textDate = itemView.tvTestDate as TextView
            progressBar = itemView.resultProgressbar as ProgressBar
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TestHistoryView {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_test_history, viewGroup, false)
        return TestHistoryView(view)
    }

    override fun getItemCount(): Int {
        return listHistory!!.size
    }

    private var testTime = 0
    private var takenMinutes = ""
    private var takenSeconds = ""
    private var point = 0
    private fun formatTime() {
        takenMinutes = (testTime / 60).toString()
        takenSeconds = (testTime % 60).toString()
    }

    override fun onBindViewHolder(view: TestHistoryView, position: Int) {
        testTime = listHistory?.get(position)?.timeTaken!!
        point = listHistory?.get(position)?.resultPoint!!
        formatTime()
        view.textTime.text = "${takenMinutes}m : ${takenSeconds}s"
        view.textPoint.text = point.toString()
        view.textDate.text = listHistory?.get(position)?.dateAttend

        //Display progress bar
        view.progressBar.max = 100
        view.progressBar.progress = point
        if (point <= 50) {
            view.progressBar.progressDrawable =
                ContextCompat.getDrawable(context, R.drawable.custom_progressbar_low)
        } else if (point > 50 && point < 80) {
            view.progressBar.progressDrawable =
                ContextCompat.getDrawable(context, R.drawable.custom_progressbar_mid)
        } else if (point >= 80 && point <= 100) {
            view.progressBar.progressDrawable =
                ContextCompat.getDrawable(context, R.drawable.custom_progressbar_high)
        }
    }


}