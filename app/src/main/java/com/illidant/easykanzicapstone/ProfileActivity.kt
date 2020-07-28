package com.illidant.easykanzicapstone

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.illidant.easykanzicapstone.domain.request.ChangePasswordRequest
import com.illidant.easykanzicapstone.domain.request.ResetPasswordRequest
import com.illidant.easykanzicapstone.extension.isNotEmptyAndBlank
import com.illidant.easykanzicapstone.platform.api.RetrofitService
import com.illidant.easykanzicapstone.platform.repository.UserRepository
import com.illidant.easykanzicapstone.platform.source.local.SharedPrefs
import com.illidant.easykanzicapstone.platform.source.local.UserLocalDataSource
import com.illidant.easykanzicapstone.platform.source.remote.UserRemoteDataSource
import com.illidant.easykanzicapstone.ui.screen.change_password.ChangePassContract
import com.illidant.easykanzicapstone.ui.screen.change_password.ChangePassPresenter
import com.illidant.easykanzicapstone.ui.screen.entry.EntryActivity
import com.illidant.easykanzicapstone.ui.screen.home.HomeActivity
import com.illidant.easykanzicapstone.ui.screen.signin.SigninPresenter
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity(), ChangePassContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        configViews()
        signOut()
        changePassword()
    }

    private val changepassPresenter by lazy {
        val retrofit = RetrofitService.getInstance(application).getService()
        val local = UserLocalDataSource.getInstance(SharedPrefs(this))
        val remote = UserRemoteDataSource(retrofit)
        val repository = UserRepository(local, remote)
        ChangePassPresenter(this, repository)
    }

    private fun signOut() {
        //Sign out button
        buttonSignout.setOnClickListener {
            val prefs = SharedPrefs(this)
            prefs.clear()
            navigateToSignin()
        }
    }
    private fun navigateToSignin() {
        EntryActivity.getIntent(this).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
        finish()
    }
    private fun changePassword() {
        txtchangepassword.setOnClickListener {
            val dialogChangePass = Dialog(this)
            dialogChangePass.setCancelable(true)
            dialogChangePass.setContentView(R.layout.dialog_change_password)
            val buttonSave= dialogChangePass.findViewById(R.id.btnSaveChange) as Button
            val edtEmail = dialogChangePass.findViewById(R.id.edtEmail) as EditText
            val edtOldPassword = dialogChangePass.findViewById(R.id.edt_old_password) as EditText
            val edtNewPassword = dialogChangePass.findViewById(R.id.edt_new_password) as EditText
            dialogChangePass.show()

            buttonSave.setOnClickListener {
                val username = edtEmail.text.toString()
                val current_password = edtOldPassword.text.toString()
                val new_password = edtNewPassword.text.toString()
                if (!username.isNotEmptyAndBlank()) {
                    edtEmail.setError("Email is required")
                    edtEmail.requestFocus()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                    edtEmail.setError("Enter a valid email")
                    edtEmail.requestFocus()
                } else if (!current_password.isNotEmptyAndBlank()) {
                    edtOldPassword.setError("Current password is required")
                    edtOldPassword.requestFocus()
                } else if (current_password.length < 6) {
                    edtOldPassword.setError("Password should be at least 6 character or more")
                    edtOldPassword.requestFocus()
                }else if (!new_password.isNotEmptyAndBlank()) {
                    edtNewPassword.setError("New password is required")
                    edtNewPassword.requestFocus()
                } else if (new_password.length < 6) {
                    edtNewPassword.setError("Password should be at least 6 character or more")
                    edtNewPassword.requestFocus()
                } else {
                    val request = ChangePasswordRequest(username, current_password, new_password)
                    changepassPresenter.changePass(request)
                    dialogChangePass.dismiss()
                }
            }

        }
    }
    override fun onChangePassSucceeded(message: String) {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        dialog.contentText = message
        dialog.show()
    }

    override fun onChangePassFail(message: String) {
        //Display error dialog
        val errDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        errDialog.contentText = message
        errDialog.show()
    }

    private fun configViews() {
        //set bottom navigation bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        //set home selected
        bottomNavigationView.selectedItemId = R.id.profile


        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true

                }
                R.id.search -> {
                    startActivity(Intent(applicationContext, SearchActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true

                }
                R.id.ranking -> {
                    startActivity(Intent(applicationContext, RankingActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true

                }
                R.id.profile -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }

}