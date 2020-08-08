package com.illidant.easykanzicapstone.ui.screen.signin

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.illidant.easykanzicapstone.BaseActivity
import com.illidant.easykanzicapstone.ui.screen.home.HomeActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.ForgotPasswordRequest
import com.illidant.easykanzicapstone.domain.request.ResetPasswordRequest
import com.illidant.easykanzicapstone.domain.request.SigninRequest
import com.illidant.easykanzicapstone.extension.isNotEmptyAndBlank
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.UserRepository
import com.illidant.easykanzicapstone.platform.source.local.SharedPrefs
import com.illidant.easykanzicapstone.platform.source.local.UserLocalDataSource
import com.illidant.easykanzicapstone.platform.source.remote.UserRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.forgot_password.ForgotPassContract
import com.illidant.easykanzicapstone.ui.screen.forgot_password.ForgotPassPresenter
import com.illidant.easykanzicapstone.ui.screen.reset_password.ResetPassContract
import com.illidant.easykanzicapstone.ui.screen.reset_password.ResetPassPresenter
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_signin.btnBack
import kotlinx.android.synthetic.main.activity_signin.edtEmail
import kotlinx.android.synthetic.main.activity_signin.edtPassword

class SigninActivity : BaseActivity(), SigninContract.View, ResetPassContract.View, ForgotPassContract.View {

    private val signInPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val local = UserLocalDataSource.getInstance(SharedPrefs(this))
        val remote = UserRemoteDataSource(retrofit)
        val repository = UserRepository(local, remote)
        SigninPresenter(this, repository)
    }


    private val resetPassPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val local = UserLocalDataSource.getInstance(SharedPrefs(this))
        val remote = UserRemoteDataSource(retrofit)
        val repository = UserRepository(local, remote)
        ResetPassPresenter(this, repository)
    }

    private val forgotPassPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val local = UserLocalDataSource.getInstance(SharedPrefs(this))
        val remote = UserRemoteDataSource(retrofit)
        val repository = UserRepository(local, remote)
        ForgotPassPresenter(this, repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        configViews()

    }
    private fun validateSignin() {
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()
        if (!email.isNotEmptyAndBlank()) {
            edtEmail.setError("Email is required")
            edtEmail.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Enter a valid email")
            edtEmail.requestFocus()
        } else if (!password.isNotEmptyAndBlank()) {
            edtPassword.setError("Password is required")
            edtPassword.requestFocus()
        } else if (password.length < 6) {
            edtPassword.setError("Password should be at least 6 character or more")
            edtPassword.requestFocus()
        } else {
            val request = SigninRequest(email, password)
            signInPresenter.signin(request)
        }
    }

    private fun popupInternetError() {
        val errDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        errDialog.contentText = "No internet connection! "
        errDialog.show()
    }

    private fun configViews() {
        //For check internet
        val connectivityManager = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo

        btnBack.setOnClickListener { finish() }

        btnSignin.setOnClickListener {
                validateSignin()
        }

        tvForgotPassword.setOnClickListener {
            val dialogForgotPass = Dialog(this)
            dialogForgotPass.setCancelable(true)
            dialogForgotPass.setContentView(R.layout.dialog_forgot_password)
            val buttonOK= dialogForgotPass.findViewById(R.id.btnOK) as TextView
            val editEmailForgot = dialogForgotPass.findViewById(R.id.edtEmailForgot) as EditText
            val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
            dialogForgotPass.show()

            buttonOK.setOnClickListener {
                val email = editEmailForgot.text.toString()
                if (!email.isNotEmptyAndBlank()) {
                    editEmailForgot.setError("Email is required")
                    editEmailForgot.requestFocus()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editEmailForgot.setError("Enter a valid email")
                    editEmailForgot.requestFocus()
                } else {
                    prefs.edit().putString("emailForgot", email).apply()
                    val forgetRequest = ForgotPasswordRequest(email)
                    forgotPassPresenter.forgotPass(forgetRequest)
                    dialogForgotPass.dismiss()
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
    private fun showResetPassDialog(){
        val dialogResetPass = Dialog(this)
        dialogResetPass.setContentView(R.layout.dialog_reset_password)
        val buttonOK= dialogResetPass.findViewById(R.id.btnResetOK) as TextView
        val emailForgot = dialogResetPass.findViewById(R.id.tvEmailForgot) as TextView
        val editOtpCode = dialogResetPass.findViewById(R.id.edtOtpCode) as EditText
        val editNewPassword = dialogResetPass.findViewById(R.id.edtNewPassword) as EditText
        val editCfNewPassword = dialogResetPass.findViewById(R.id.edtCfNewPassword) as EditText
        val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        val saveEmailForgot = prefs.getString("emailForgot", null)
        emailForgot.text = "Email: ${saveEmailForgot}"
        dialogResetPass.show()

        buttonOK.setOnClickListener {
            val otpCode = editOtpCode.text.toString()
            val newPassword = editNewPassword.text.toString()
            val cfNewPassword = editCfNewPassword.text.toString()
            if (!otpCode.isNotEmptyAndBlank()) {
                editOtpCode.setError("OTP code is required")
                editOtpCode.requestFocus()
            }else if (otpCode.length != 8){
                editOtpCode.setError("OTP code is required 8 character")
                editOtpCode.requestFocus()
            }else if (!newPassword.isNotEmptyAndBlank()) {
                editNewPassword.setError("New password is required")
                editNewPassword.requestFocus()
            } else if(newPassword.length < 6) {
                editNewPassword.setError("Password should be at least 6 character or more")
                editNewPassword.requestFocus()
            }else if (!cfNewPassword.isNotEmptyAndBlank()) {
                editCfNewPassword.setError("Confirm password is required")
                editCfNewPassword.requestFocus()
            } else if (!cfNewPassword.equals(newPassword)) {
                editCfNewPassword.setError("Not match password. Please re-enter")
                editCfNewPassword.requestFocus()
            } else {
                val resetRequest = ResetPasswordRequest(saveEmailForgot.toString(),cfNewPassword,otpCode)
                resetPassPresenter.resetPass(resetRequest)
                dialogResetPass.dismiss()
            }

        }
    }

    override fun onSigninSucceeded(user: User) {
        val prefs: SharedPreferences = getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("userID", user.id).apply()
        prefs.edit().putString("userName", user.username).apply()
        navigateToHome()
    }

    override fun onSigninFailed(exception: Throwable) {
        val errDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        errDialog.contentText = "Email or password is not correct !"
        errDialog.show()
    }

    // Reset passsword success
    override fun onResetPassSucceeded(message: String) {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        dialog.contentText = message
        dialog.show()
    }
    // Reset passsword fail
    override fun onResetPassFail(message: String) {
        val errDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        errDialog.contentText = message
        errDialog.show()

    }
    // Forgot password success
    override fun onForgotPassSucceeded(message: String) {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        dialog.contentText = message
        dialog.setCancelable(false)
        dialog.show()
        dialog.setConfirmClickListener {
            showResetPassDialog()
            dialog.dismiss()
        }
    }
    // Forgot password fail
    override fun onForgotPassFail(message: String) {
        //Display error dialog
        val errDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        errDialog.contentText = message
        errDialog.show()
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, SigninActivity::class.java)
    }
}