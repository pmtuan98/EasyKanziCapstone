package com.illidant.easykanzicapstone.ui.screen.signin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.ui.screen.login.LoginActivity
import com.illidant.easykanzicapstone.ui.screen.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        buttonSignIn.setOnClickListener {
            startActivity(LoginActivity.getIntent(this))
        }

        textRegister.setOnClickListener {
            startActivity(RegisterActivity.getIntent(this))
        }
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, SignInActivity::class.java)
    }
}
