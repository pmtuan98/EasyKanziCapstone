package com.illidant.easykanzicapstone.ui.screen.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.request.LoginRequest
import com.illidant.easykanzicapstone.domain.request.RegisterRequest
import com.illidant.easykanzicapstone.extension.isNotEmptyAndBlank
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.UserRepository
import com.illidant.easykanzicapstone.platform.source.local.SharedPrefs
import com.illidant.easykanzicapstone.platform.source.local.UserLocalDataSource
import com.illidant.easykanzicapstone.platform.source.remote.UserRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.login.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.buttonBack
import kotlinx.android.synthetic.main.activity_login.editEmail
import kotlinx.android.synthetic.main.activity_login.editPassword
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity: AppCompatActivity(), RegisterContract.View {
    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val local = UserLocalDataSource.getInstance(SharedPrefs(this))
        val remote = UserRemoteDataSource(retrofit)
        val repository = UserRepository(local, remote)
        RegisterPresenter(this, repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        configViews()
    }
    private fun configViews() {
        buttonBack.setOnClickListener { finish() }

        buttonSignup.setOnClickListener {
            val username = editEmail.text.toString()
            val password = editPassword.text.toString()
            if (username.isNotEmptyAndBlank() && password.isNotEmptyAndBlank()) {
                val request = RegisterRequest(username,password)
                presenter.register(request)
            }
        }
    }


    companion object {
        fun getIntent(context: Context) = Intent(context, RegisterActivity::class.java)
    }

    override fun onRegisterSucceeded(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
        Log.e("onRegisterSucceeded", message)
    }

    override fun onRegisterFailed(exception: Throwable) {
        Log.e("onRegisterFailed", exception.toString())
    }
}