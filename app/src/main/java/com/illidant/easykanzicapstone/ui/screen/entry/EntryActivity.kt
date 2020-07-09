package com.illidant.easykanzicapstone.ui.screen.entry

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.illidant.easykanzicapstone.ui.screen.home.HomeActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.platform.source.local.SharedPrefs
import com.illidant.easykanzicapstone.ui.screen.signin.SigninActivity
import com.illidant.easykanzicapstone.ui.screen.signup.SignupActivity
import kotlinx.android.synthetic.main.activity_entry.*

class EntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        val prefs = SharedPrefs(this)
        if (!prefs.token.isNullOrBlank()) {
            navigateToHome()
        }

        buttonSignIn.setOnClickListener {
            startActivity(SigninActivity.getIntent(this))
        }

        textRegister.setOnClickListener {
            startActivity(SignupActivity.getIntent(this))
        }
    }

    private fun navigateToHome() {
        HomeActivity.getIntent(this).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
        finish()
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, EntryActivity::class.java)
    }
}
