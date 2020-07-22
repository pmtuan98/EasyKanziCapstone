package com.illidant.easykanzicapstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
  private val SPLASH_TIMER = 3000;

  //Animation
  var sideAnim: Animation? = null;
  var bottomAnim: Animation? = null;

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    setContentView(R.layout.activity_main)

    //Animation
    sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim)
    bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim)


    //set animation on elements
    powered_by_line.setAnimation(bottomAnim)
    intro_text.setAnimation(sideAnim)

    Handler().postDelayed({
      val intent = Intent(applicationContext, OnBoardingActivity::class.java)
      startActivity(intent)
      finish()
    },SPLASH_TIMER.toLong())
  }
}
