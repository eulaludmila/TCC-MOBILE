package tcc.sp.senai.br.showdebolos


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreen : AppCompatActivity() {

    val SPLASH_DELAY: Long = 2000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val pulse = AnimationUtils.loadAnimation(this, R.anim.pulse)
        img_splash_logo.startAnimation(pulse)


        Handler().postDelayed({
            val intent = Intent(this, CircularReveal::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            finish()

        }, SPLASH_DELAY)

    }

}