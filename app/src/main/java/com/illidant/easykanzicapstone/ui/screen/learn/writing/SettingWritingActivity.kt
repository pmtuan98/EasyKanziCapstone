package com.illidant.easykanzicapstone.ui.screen.learn.writing

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.ui.screen.learn.LearnActivity
import kotlinx.android.synthetic.main.activity_level.*
import kotlinx.android.synthetic.main.bottom_navigation_bar.*
import kotlinx.android.synthetic.main.setting_learn_writing_layout.*


class SettingWritingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_learn_writing_layout)
        radioState()
        radioSetting()

    }

    private fun radioSetting() {
        buttonSaveWritingMode.setOnClickListener{
            if (radioHira.isChecked) {
                val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
                prefs.edit().putBoolean("hiraState", true).apply()
                prefs.edit().putBoolean("vnState", false).apply()
                val intent = Intent(this, WritingActivity::class.java)
                startActivity(intent)
                finish()
            }else if(radioVN.isChecked) {
                val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
                prefs.edit().putBoolean("vnState", true).apply()
                prefs.edit().putBoolean("hiraState", false).apply()
                // Switch vietnamese mode in writing
                val intent = Intent(this, WritingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
    private fun radioState() {
        val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        radioHira.isChecked = prefs.getBoolean("hiraState",radioHira.isChecked)
        radioVN.isChecked = prefs.getBoolean("vnState", radioVN.isChecked)

    }

}