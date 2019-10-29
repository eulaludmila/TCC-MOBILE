package tcc.sp.senai.br.showdebolos

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_circular_reveal.*
import kotlinx.android.synthetic.main.activity_main.*

class CircularReveal : AppCompatActivity() {


    var mPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0,0)
        setContentView(R.layout.activity_circular_reveal)

        if (savedInstanceState == null) {
            root_layout.setVisibility(View.INVISIBLE)

            val viewTreeObserver = root_layout.getViewTreeObserver()
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        circularRevealActivity()
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            root_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this)
                        } else {
                            root_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                        }
                    }
                })
            }
        }

        Handler().postDelayed({
            checkSharedPreferences()
        }, 200)

    }

    private fun circularRevealActivity() {

        val cx = root_layout.getWidth() / 2
        val cy = root_layout.getHeight() / 2

        val finalRadius = Math.max(root_layout.getWidth(), root_layout.getHeight())

        // create the animator for this view (the start radius is zero)
        val circularReveal = ViewAnimationUtils.createCircularReveal(root_layout, cx, cy, 0f, finalRadius.toFloat())
        circularReveal.duration = 1000

        // make the view visible and start the animation
        root_layout.setVisibility(View.VISIBLE)
        circularReveal.start()
    }

    fun checkSharedPreferences(){

        mPreferences = getSharedPreferences("idValue",0)
        val token = mPreferences!!.getString("token", "")

        if(token != ""){
            val intent = Intent(this, MainActivityFragment::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            overridePendingTransition(0,0)
            finish()
        }

    }

}
