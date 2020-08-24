package com.illidant.easykanzicapstone.ui.screen.test

import android.content.Intent
import android.os.Bundle
import com.illidant.easykanzicapstone.BaseActivity
import com.illidant.easykanzicapstone.R
import kotlinx.android.synthetic.main.entry_test_layout.*

class EntryTestActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.entry_test_layout)
        initialize()
    }

    private fun initialize() {
        val levelName = intent.getStringExtra("LEVEL_NAME")
        val levelId = intent.getIntExtra("LEVEL_ID", 0)
        tvTestLevel.text = levelName
        btnStart.setOnClickListener {
            val intent = Intent(it.context, TestActivity::class.java)
            intent.putExtra("LEVEL_ID", levelId)
            intent.putExtra("LEVEL_NAME", levelName)
            startActivity(intent)
            finish()
        }
        btnBack.setOnClickListener {
            finish()
        }
    }
}