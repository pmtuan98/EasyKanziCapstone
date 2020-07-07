package com.illidant.easykanzicapstone.ui.screen.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.request.RegisterRequest
import com.illidant.easykanzicapstone.extension.isNotEmptyAndBlank
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.UserRepository
import com.illidant.easykanzicapstone.platform.source.local.SharedPrefs
import com.illidant.easykanzicapstone.platform.source.local.UserLocalDataSource
import com.illidant.easykanzicapstone.platform.source.remote.UserRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.login.LoginActivity
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
            val cf_password = editConfirmPassword.text.toString()
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
            }else if (!cf_password.isNotEmptyAndBlank()) {
                editConfirmPassword.setError("Confirm password is required")
                editConfirmPassword.requestFocus()
            } else if (!cf_password.equals(password)) {
                editConfirmPassword.setError("Not match password. Please re-enter")
                editConfirmPassword.requestFocus()
            } else {
                val request = RegisterRequest(username,password)
                presenter.register(request)
            }
        }
    }


    companion object {
        fun getIntent(context: Context) = Intent(context, RegisterActivity::class.java)
    }

    override fun onRegisterSucceeded(message: String) {
        //Display successfully dialog
        val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = "Sign up successfully !"
        dialog.setCancelable(false)
        dialog.show()
        dialog.setConfirmClickListener {
            val intoSignin = Intent(this, LoginActivity::class.java)
            startActivity(intoSignin)
            finish()
        }
    }

    override fun onRegisterFailed(exception: Throwable) {
        //Display error dialog
        val errDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        errDialog.contentText = "Email already exist, please try again !"
        errDialog.show()
    }
}