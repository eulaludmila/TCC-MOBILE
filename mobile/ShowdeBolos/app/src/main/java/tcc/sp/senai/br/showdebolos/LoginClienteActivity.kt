package tcc.sp.senai.br.showdebolos

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login_cliente.*

class LoginClienteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_cliente)

        txt_cadastro_cliente.setOnClickListener {
            val intent = Intent(this, CadastroClienteActivity::class.java)
            startActivity(intent)
        }

    }
}
