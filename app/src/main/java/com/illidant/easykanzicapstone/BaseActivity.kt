package com.illidant.easykanzicapstone

import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var mSnackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }


    private fun showMessage(isConnected: Boolean) {

        if (!isConnected) {
            val messageToUser = "Please check your internet connection!"
            mSnackBar = Snackbar.make(
                findViewById(R.id.rootLayout),
                messageToUser,
                Snackbar.LENGTH_LONG
            ) //Assume "rootLayout" as the root layout of every activity.
            mSnackBar?.duration = Snackbar.LENGTH_INDEFINITE
            mSnackBar!!.view.setBackgroundColor(
                ContextCompat.getColor(
                    this!!,
                    R.color.snackbar_red
                )
            )
            mSnackBar!!.setActionTextColor(Color.WHITE)
            mSnackBar?.show()
        } else {
            mSnackBar?.dismiss()
        }

    }

    override fun onResume() {
        super.onResume()

        ConnectivityReceiver.connectivityReceiverListener = this
    }

    /**
     * Callback will be called when there is change
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }
}