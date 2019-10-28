package tcc.sp.senai.br.showdebolos

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login_cliente.*
import tcc.sp.senai.br.showdebolos.model.Celular
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.showdebolos.tasks.LoginClienteTasks
import java.lang.RuntimeException

class LoginClienteActivity : AppCompatActivity() {

    var mPreferences:SharedPreferences? = null
    var mEditor:SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_cliente)

        txt_cadastro_cliente.setOnClickListener {
            val intent = Intent(this, CadastroClienteActivity::class.java)
            startActivity(intent)
        }

        btn_entrar_cliente.setOnClickListener {

            val cliente = Cliente(0,"","","","",txt_email_cliente.text.toString(),txt_senha_cliente.text.toString(), Celular(0,""),"","")


            if(txt_email_cliente.text.toString() != null && txt_senha_cliente.text.toString() != null){

                    val loginCliente = LoginClienteTasks(cliente, this)
                    loginCliente.execute()
                    val retornoLogin = loginCliente.get()
                if(retornoLogin != "erro"){

                    mPreferences = getSharedPreferences("idValue", 0)

                    mEditor = mPreferences!!.edit()
                    mEditor!!.putString("token",retornoLogin)
                    mEditor!!.commit()
                    val intent = Intent(this, MainActivityFragment::class.java)
                    startActivity(intent)
                }else{
                    val builder = AlertDialog.Builder(this@LoginClienteActivity)
                    builder.setTitle("ERRO")
                    builder.setIcon(R.drawable.ic_erro)
                    builder.setMessage("E-mail ou Senha estão incorretos.")
                    builder.setPositiveButton("OK"){dialog, which ->  }
                    builder.show()
                }

            }else{
                val builder = AlertDialog.Builder(this@LoginClienteActivity)
                builder.setTitle("ERRO")
                builder.setIcon(R.drawable.ic_erro)
                builder.setMessage("Digite seu e-mail ou senha.")
                builder.setPositiveButton("OK"){dialog, which ->  }
                builder.show()
            }



        }


    }



}
