package com.illidant.easykanzicapstone

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.ViewPager
import com.illidant.easykanzicapstone.platform.source.local.SharedPrefs
import com.illidant.easykanzicapstone.ui.screen.entry.EntryActivity
import com.illidant.easykanzicapstone.ui.screen.home.HomeActivity
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : AppCompatActivity() {

    private var changeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                addDots(position)
                when (position) {
                    0 -> {
                        btnGetStarted.visibility = View.INVISIBLE
                    }
                    1 -> {
                        btnGetStarted.visibility = View.INVISIBLE
                    }
                    else -> {
                        btnGetStarted.visibility = View.VISIBLE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = SharedPrefs(this)
        if (!prefs.token.isNullOrBlank()) {
            navigateToHome()
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_on_boarding)
        //Hooks
        //Call adapter
        slider.adapter = SliderAdapter(this)

        addDots(0)
        slider.addOnPageChangeListener(changeListener)

        //Button get started
        btnGetStarted.setOnClickListener {
            val intent = EntryActivity.getIntent(this).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
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


    private fun addDots(position: Int) {
        val dots = arrayOfNulls<TextView>(3)
        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]?.text = HtmlCompat.fromHtml("&#8226", HtmlCompat.FROM_HTML_MODE_LEGACY)
            dots[i]?.textSize = 35f
            dotsLayout.addView(dots[i])
        }
        if (dots.isNotEmpty()) {
            dots[position]?.setTextColor(resources.getColor(R.color.color_app, null))
        }
    }


}