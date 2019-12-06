package tcc.sp.senai.br.showdebolos

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_recusado_pedido.*
import kotlinx.android.synthetic.main.activity_sucesso_pedido.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class RecusadoPedidoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recusado_pedido)

        val crescer = AnimationUtils.loadAnimation(this, R.anim.crescer)
        img_recusado_pedido.startAnimation(crescer)
        txt_recusado_pedido.startAnimation(crescer)


        card.setOnClickListener {

            val intent = Intent(this, MainActivityFragment::class.java)
            intent.putExtra("pedido" , "A")
            startActivity(intent)
            finish()

        }

    }
}
