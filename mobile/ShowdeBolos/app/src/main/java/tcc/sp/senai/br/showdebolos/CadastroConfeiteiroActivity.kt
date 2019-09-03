package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import kotlinx.android.synthetic.main.activity_cadastro_cliente.*
import kotlinx.android.synthetic.main.activity_cadastro_confeiteiro.*
import tcc.sp.senai.br.showdebolos.R.id.*

class CadastroConfeiteiroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_confeiteiro)

        txt_cpf_confeiteiro.addTextChangedListener(Mask.mask("###.###.###-##", txt_cpf_confeiteiro))
        txt_celular_confeiteiro.addTextChangedListener(Mask.mask("(##) #####-####", txt_celular_confeiteiro))
        txt_dt_nascimento_confeiteiro.addTextChangedListener(Mask.mask("##/##/####", txt_celular_confeiteiro))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.context_menu, menu)

        return true
    }
}
