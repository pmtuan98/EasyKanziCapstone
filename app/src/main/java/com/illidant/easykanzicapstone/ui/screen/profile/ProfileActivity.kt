package com.illidant.easykanzicapstone.ui.screen.profile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.illidant.easykanzicapstone.BaseActivity
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.ui.screen.ranking.RankingActivity
import com.illidant.easykanzicapstone.ui.screen.search.SearchActivity
import com.illidant.easykanzicapstone.domain.request.ChangePasswordRequest
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
import com.illidant.easykanzicapstone.ui.screen.profile.test_history.TestHistoryActivity
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity(), ChangePassContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initialize()
        configViews()
        signOut()
        changePassword()
        navigateToTestHistory()
    }

    private fun initialize() {
        val prefs: SharedPreferences =
            getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
        val userName = prefs.getString("userName", null)
        var userEmail = prefs.getString("userEmail", null)
        tvUsername.text = userName
        tvUserEmail.text = userEmail
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
        layoutSignOut.setOnClickListener {
            val prefs = SharedPrefs(this)
            prefs.clear()
            navigateToSignin()
        }
    }

    private fun navigateToTestHistory() {
        layoutTestHistory.setOnClickListener {
            val intent = Intent(it.context, TestHistoryActivity::class.java)
            startActivity(intent)
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
        layoutChangePass.setOnClickListener {
            val prefs: SharedPreferences =
                getSharedPreferences("com.illidant.kanji.prefs", Context.MODE_PRIVATE)
            val email = prefs.getString("userEmail", null)
            val dialogChangePass = Dialog(this)
            dialogChangePass.setCancelable(true)
            dialogChangePass.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialogChangePass.setContentView(R.layout.dialog_change_password)
            val buttonSave = dialogChangePass.findViewById(R.id.btnSave) as TextView
            val textEmail = dialogChangePass.findViewById(R.id.tvEmail) as TextView
            textEmail.text = "${email}"
            val edtOldPassword = dialogChangePass.findViewById(R.id.edtOldPassword) as EditText
            val edtNewPassword = dialogChangePass.findViewById(R.id.edtNewPassword) as EditText
            val edtCfNewPassword = dialogChangePass.findViewById(R.id.edtCfPassword) as EditText
            dialogChangePass.show()

            buttonSave.setOnClickListener {

                val current_password = edtOldPassword.text.toString()
                val new_password = edtNewPassword.text.toString()
                val cf_password = edtCfNewPassword.text.toString()
                if (!current_password.isNotEmptyAndBlank()) {
                    edtOldPassword.setError("Current password is required")
                    edtOldPassword.requestFocus()
                } else if (current_password.length < 6) {
                    edtOldPassword.setError("Password should be at least 6 character or more")
                    edtOldPassword.requestFocus()
                } else if (!new_password.isNotEmptyAndBlank()) {
                    edtNewPassword.setError("New password is required")
                    edtNewPassword.requestFocus()
                } else if (new_password.length < 6) {
                    edtNewPassword.setError("Password should be at least 6 character or more")
                    edtNewPassword.requestFocus()
                } else if (!cf_password.isNotEmptyAndBlank()) {
                    edtCfNewPassword.setError("Confirm password is required")
                    edtCfNewPassword.requestFocus()
                } else if (cf_password.length < 6) {
                    edtCfNewPassword.setError("Password should be at least 6 character or more")
                    edtCfNewPassword.requestFocus()
                } else if (!cf_password.equals(new_password)) {
                    edtCfNewPassword.setError("Not match password. Please re-enter")
                    edtCfNewPassword.requestFocus()
                } else {
                    val request =
                        ChangePasswordRequest(email.toString(), new_password, current_password)
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

    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun configViews() {
        //set home selected
        bottomNavBar.selectedItemId = R.id.profile
        //Perform ItemSelectedListener
        bottomNavBar.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
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