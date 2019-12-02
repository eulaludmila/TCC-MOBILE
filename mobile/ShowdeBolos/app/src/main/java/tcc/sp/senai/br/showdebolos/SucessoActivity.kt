package tcc.sp.senai.br.showdebolos

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_sucesso.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class SucessoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sucesso)

        val crescer = AnimationUtils.loadAnimation(this, R.anim.crescer)
        img_done.startAnimation(crescer)
        txt_sucesso.startAnimation(crescer)

        viewKonfetti.build()
                .addColors(Color.WHITE, Color.RED, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(5f, 12f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(Size(12))
                .setPosition(180f, viewKonfetti.width + 0f, -0f, -0f)
                .streamFor(300, 800L)


        Handler().postDelayed({

            val intent = Intent(this,LoginClienteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            overridePendingTransition(0,0)
            finish()
        },2500)


    }

}