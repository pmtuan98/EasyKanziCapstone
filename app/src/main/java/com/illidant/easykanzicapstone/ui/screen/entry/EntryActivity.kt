package com.illidant.easykanzicapstone.ui.screen.entry

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.ui.screen.signin.SigninActivity
import com.illidant.easykanzicapstone.ui.screen.signup.SignupActivity
import kotlinx.android.synthetic.main.activity_entry.*

class EntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        btnSignin.setOnClickListener {
            startActivity(SigninActivity.getIntent(this))
        }

        tvSignup.setOnClickListener {
            startActivity(SignupActivity.getIntent(this))
        }
    }
    
    companion object {
        fun getIntent(context: Context) = Intent(context, EntryActivity::class.java)
    }
}
