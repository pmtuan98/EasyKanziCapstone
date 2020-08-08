package com.illidant.easykanzicapstone.ui.screen.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.illidant.easykanzicapstone.R
import kotlinx.android.synthetic.main.entry_test_layout.*

class EntryTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.entry_test_layout)
        initialize()
    }

    private fun initialize() {
        tvTestLevel.text = intent.getStringExtra("LEVEL_NAME")
        val level_id = intent.getIntExtra("LEVEL_ID", 0)
        btnStart.setOnClickListener {
            val intent = Intent(it.context, TestActivity::class.java)
            intent.putExtra("LEVEL_ID", level_id)
            startActivity(intent)
            finish()
        }
    }
}