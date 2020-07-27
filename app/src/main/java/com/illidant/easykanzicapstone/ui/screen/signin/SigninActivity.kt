package com.illidant.easykanzicapstone.ui.screen.signin

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.illidant.easykanzicapstone.ui.screen.home.HomeActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.ResetPasswordRequest
import com.illidant.easykanzicapstone.domain.request.SigninRequest
import com.illidant.easykanzicapstone.extension.isNotEmptyAndBlank
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.UserRepository
import com.illidant.easykanzicapstone.platform.source.local.SharedPrefs
import com.illidant.easykanzicapstone.platform.source.local.UserLocalDataSource
import com.illidant.easykanzicapstone.platform.source.remote.UserRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.forget_password.ResetPassContract
import com.illidant.easykanzicapstone.ui.screen.forget_password.ResetPassPresenter
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.dialog_forgot_password.*

class SigninActivity : AppCompatActivity(), SigninContract.View, ResetPassContract.View {

    private val presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val local = UserLocalDataSource.getInstance(SharedPrefs(this))
        val remote = UserRemoteDataSource(retrofit)
        val repository = UserRepository(local, remote)
        SigninPresenter(this, repository)
    }
    private val reset_pass_presenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val local = UserLocalDataSource.getInstance(SharedPrefs(this))
        val remote = UserRemoteDataSource(retrofit)
        val repository = UserRepository(local, remote)
        ResetPassPresenter(this, repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        configViews()

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
            } else if (password.length < 6) {
                editPassword.setError("Password should be at least 6 character or more")
                editPassword.requestFocus()
            } else {
                val request = SigninRequest(username, password)
                presenter.signin(request)
            }

        }

        txtForgotPass.setOnClickListener {
            val dialogResetPass = Dialog(this)
            dialogResetPass.setCancelable(true)
            dialogResetPass.setContentView(R.layout.dialog_forgot_password)
            val buttonSave= dialogResetPass.findViewById(R.id.buttonSaveNewPass) as Button
            val editEmailForgot = dialogResetPass.findViewById(R.id.editEmailForgot) as EditText
            val editPasswordForgot = dialogResetPass.findViewById(R.id.editPasswordForgot) as EditText
            dialogResetPass.show()

            buttonSave.setOnClickListener {
                val username = editEmailForgot.text.toString()
                val new_password = editPasswordForgot.text.toString()
                if (!username.isNotEmptyAndBlank()) {
                    editEmailForgot.setError("Email is required")
                    editEmailForgot.requestFocus()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                    editEmailForgot.setError("Enter a valid email")
                    editEmailForgot.requestFocus()
                } else if (!new_password.isNotEmptyAndBlank()) {
                    editPasswordForgot.setError("New password is required")
                    editPasswordForgot.requestFocus()
                } else if (new_password.length < 6) {
                    editPasswordForgot.setError("Password should be at least 6 character or more")
                    editPasswordForgot.requestFocus()
                } else {
                    val resetRequest = ResetPasswordRequest(username, new_password)
                    reset_pass_presenter.resetPass(resetRequest)
                }
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
    }


    override fun onResetPassSucceeded(message: String) {

        val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        dialog.contentText = message
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onResetPassFail(message: String) {
        //Display error dialog
        val errDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        errDialog.contentText = "Email is not exist"
        errDialog.show()

    }

    companion object {
        fun getIntent(context: Context) = Intent(context, SigninActivity::class.java)
    }
}