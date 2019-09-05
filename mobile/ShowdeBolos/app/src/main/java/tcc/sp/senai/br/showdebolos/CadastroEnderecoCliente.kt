package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_cadastro_endereco_cliente.*

class CadastroEnderecoCliente : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_endereco_cliente)

        txt_cep_cliente.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                txt_cep_cliente.hint = "Ex: 00000-000"
            }else{
                txt_cep_cliente.hint = ""
            }
        }

    }
}
