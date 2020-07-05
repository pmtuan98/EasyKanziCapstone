package com.illidant.easykanzicapstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : AppCompatActivity() {
    var viewPager: ViewPager? = null
    var dotsLayout: LinearLayout? = null
    var sliderAdapter: SliderAdapter? = null
    var dots: Array<TextView?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding)
        //Hooks
        viewPager = findViewById(R.id.slider)
        dotsLayout = findViewById(R.id.dots)
        //Call adapter
        sliderAdapter = SliderAdapter(this)
        viewPager!!.adapter = sliderAdapter

        addDots(0)
        viewPager!!.addOnPageChangeListener(changeListener)

        //Button get started

        //Button get started
        get_started_btn.setOnClickListener(View.OnClickListener {
            val intoLogin = Intent(this@OnBoardingActivity, HomeActivity::class.java)
            startActivity(intoLogin)
        })
    }

    private fun addDots(position: Int) {
        dots = arrayOfNulls<TextView>(3)
        dotsLayout!!.removeAllViews()
        for (i in dots!!.indices) {
            dots!![i] = TextView(this)
            dots!![i]!!.text = Html.fromHtml("&#8226")
            dots!![i]!!.textSize = 35f
            dotsLayout!!.addView(dots!![i])
        }
        if (dots!!.size > 0) {
            dots!![position]!!.setTextColor(resources.getColor(R.color.colorPrimaryDark))
        }
    }

    var changeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            addDots(position)
            if (position == 0) {
                get_started_btn.setVisibility(View.INVISIBLE)
            } else if (position == 1) {
                get_started_btn.setVisibility(View.INVISIBLE)
            } else {
                get_started_btn.setVisibility(View.VISIBLE)
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
}