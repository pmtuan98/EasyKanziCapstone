package com.illidant.easykanzicapstone.ui.screen.signin

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.illidant.easykanzicapstone.ui.screen.home.HomeActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.SigninRequest
import com.illidant.easykanzicapstone.extension.isNotEmptyAndBlank
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.UserRepository
import com.illidant.easykanzicapstone.platform.source.local.SharedPrefs
import com.illidant.easykanzicapstone.platform.source.local.UserLocalDataSource
import com.illidant.easykanzicapstone.platform.source.remote.UserRemoteDataSource
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.dialog_forgot_password.*

class SigninActivity : AppCompatActivity(), SigninContract.View {

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val local = UserLocalDataSource.getInstance(SharedPrefs(this))
        val remote = UserRemoteDataSource(retrofit)
        val repository = UserRepository(local, remote)
        SigninPresenter(this, repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        configViews()
        txtForgotPass.setOnClickListener{
            executeForgotPass()
        }

    }

    private fun configViews() {
        buttonBack.setOnClickListener { finish() }

        buttonSignin.setOnClickListener {
            val username = editEmail.text.toString()
            val password = editPassword.text.toString()
           if (!username.isNotEmptyAndBlank()) {
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
            } else {
                val request = SigninRequest(username, password)
                presenter.signin(request)
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

    override fun onSigninSucceeded(user: User) {
        navigateToHome()
    }

    override fun onSigninFailed(exception: Throwable) {
        //Display error dialog
        val errDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        errDialog.contentText = "Email or password is not correct !"
        errDialog.show()
        //exception.localizedMessage?.let { toast(it) }
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, SigninActivity::class.java)
    }

    private fun executeForgotPass() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_forgot_password)
        dialog.show()
        buttonSaveNewPass.setOnClickListener {
            val username = editEmailForgot.text.toString()
            val password = editPasswordForgot.text.toString()
            if (!username.isNotEmptyAndBlank()) {
                editEmailForgot.setError("Email is required")
                editEmailForgot.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                editEmailForgot.setError("Enter a valid email")
                editEmailForgot.requestFocus()
            } else if (!password.isNotEmptyAndBlank()) {
                editPasswordForgot.setError("Password is required")
                editPasswordForgot.requestFocus()
            } else if(password.length < 6) {
                editPasswordForgot.setError("Password should be at least 6 character or more")
                editPasswordForgot.requestFocus()
            } else {
//                val request = SigninRequest(username, password)
//                presenter.signin(request)
            }
        }
    }
}