package tcc.sp.senai.br.showdebolos

import android.content.Intent

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_login_confeiteiro.*




class LoginConfeiteiroActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_confeiteiro)


        txt_cadastro_confeiteiro.setOnClickListener {
            val intent = Intent(this, CadastroConfeiteiroActivity::class.java)
            startActivity(intent)
        }


    }


}