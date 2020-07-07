package com.illidant.easykanzicapstone.ui.screen.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.illidant.easykanzicapstone.HomeActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.LoginRequest
import com.illidant.easykanzicapstone.extension.isNotEmptyAndBlank
import com.illidant.easykanzicapstone.extension.toast
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.UserRepository
import com.illidant.easykanzicapstone.platform.source.local.SharedPrefs
import com.illidant.easykanzicapstone.platform.source.local.UserLocalDataSource
import com.illidant.easykanzicapstone.platform.source.remote.UserRemoteDataSource
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val local = UserLocalDataSource.getInstance(SharedPrefs(this))
        val remote = UserRemoteDataSource(retrofit)
        val repository = UserRepository(local, remote)
        LoginPresenter(this, repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        configViews()
    }

    private fun configViews() {
        buttonBack.setOnClickListener { finish() }

        buttonLogin.setOnClickListener {
            val username = editEmail.text.toString()
            val password = editPassword.text.toString()
            if (username.isNotEmptyAndBlank() && password.isNotEmptyAndBlank()) {
                val request = LoginRequest(username, password)
                presenter.login(request)
            } else if (!username.isNotEmptyAndBlank()) {
                editEmail.setError("Email is required")
                editEmail.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                editEmail.setError("Enter a valid email")
                editEmail.requestFocus()
            } else if (!password.isNotEmptyAndBlank()) {
                editPassword.setError("Password is required")
                editPassword.requestFocus()
            } else if(password.length < 6) {
                editPassword.setError("Password should be at least 6 character or more")
                editPassword.requestFocus()
            }

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

    override fun onLoginSucceeded(user: User) {
        navigateToHome()
    }

    override fun onLoginFailed(exception: Throwable) {
        exception.localizedMessage?.let { toast(it) }
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}