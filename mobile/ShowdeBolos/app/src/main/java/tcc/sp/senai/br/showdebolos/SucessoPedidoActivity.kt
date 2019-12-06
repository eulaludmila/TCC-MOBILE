package tcc.sp.senai.br.showdebolos

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_sucesso.*
import kotlinx.android.synthetic.main.activity_sucesso_pedido.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class SucessoPedidoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sucesso_pedido)

        val crescer = AnimationUtils.loadAnimation(this, R.anim.crescer)
        img_done_pedido.startAnimation(crescer)
        txt_sucesso_pedido.startAnimation(crescer)

        viewKonfettiPedido.build()
                .addColors(Color.WHITE, Color.RED, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(5f, 12f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(Size(12))
                .setPosition(180f, viewKonfettiPedido.width + 0f, -0f, -0f)
                .streamFor(300, 800L)


        viewKonfettiPedido.setOnClickListener {

            val intent = Intent(this, MainActivityFragment::class.java)
            intent.putExtra("pedido" , "A")
            startActivity(intent)
            finish()

        }


    }
}
