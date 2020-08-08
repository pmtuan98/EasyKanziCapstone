package com.illidant.easykanzicapstone.ui.screen.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.request.SignupRequest
import com.illidant.easykanzicapstone.extension.isNotEmptyAndBlank
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.UserRepository
import com.illidant.easykanzicapstone.platform.source.local.SharedPrefs
import com.illidant.easykanzicapstone.platform.source.local.UserLocalDataSource
import com.illidant.easykanzicapstone.platform.source.remote.UserRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.signin.SigninActivity
import kotlinx.android.synthetic.main.activity_signin.buttonBack
import kotlinx.android.synthetic.main.activity_signin.editEmail
import kotlinx.android.synthetic.main.activity_signin.editPassword
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity: AppCompatActivity(), SignupContract.View {
    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val local = UserLocalDataSource.getInstance(SharedPrefs(this))
        val remote = UserRemoteDataSource(retrofit)
        val repository = UserRepository(local, remote)
        SignupPresenter(this, repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        configViews()
    }


    private fun configViews() {
        buttonBack.setOnClickListener { finish() }

        buttonSignup.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val username = editUsername.text.toString()
            val cf_password = editConfirmPassword.text.toString()
            if (!email.isNotEmptyAndBlank()) {
                editEmail.setError("Email is required")
                editEmail.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editEmail.setError("Enter a valid email")
                editEmail.requestFocus()
            } else if (!username.isNotEmptyAndBlank()) {
                editUsername.setError("Username is required")
                editUsername.requestFocus()
            } else if(username.length > 11) {
                editUsername.setError("Username max length is 10 character")
                editUsername.requestFocus()
            }else if (!password.isNotEmptyAndBlank()) {
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
                val request = SignupRequest(email,cf_password,username)
                presenter.signup(request)
            }
        }
    }


    companion object {
        fun getIntent(context: Context) = Intent(context, SignupActivity::class.java)
    }

    override fun onSignupSucceeded(message: String) {
        //Display successfully dialog
        val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = "Sign up successfully!"
        dialog.contentText = "A verified link have been sent to your email"
        dialog.setCancelable(false)
        dialog.show()
        dialog.setConfirmClickListener {
            val intoSignin = Intent(this, SigninActivity::class.java)
            startActivity(intoSignin)
            finish()
        }
    }

    override fun onSignupFailed(message: String) {
        //Display error dialog
        val errDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        errDialog.contentText = message
        errDialog.setCancelable(false)
        errDialog.show()
//        errDialog.setConfirmClickListener {
//            val intent = intent
//            finish()
//            startActivity(intent)
//        }
    }
}