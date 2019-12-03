package tcc.sp.senai.br.showdebolos

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_login_cliente.*
import org.jetbrains.anko.act
import tcc.sp.senai.br.showdebolos.model.Celular
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.showdebolos.tasks.LoginClienteTasks

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
                if(retornoLogin == "conexao"){
                    val builder = AlertDialog.Builder(this@LoginClienteActivity)
                    builder.setTitle("ERRO")
                    builder.setIcon(R.drawable.ic_erro)
                    builder.setMessage("Erro ao conectador ao servidor.")
                    builder.setMessage("Tente novamente mais tarde.")
                    builder.setPositiveButton("OK"){dialog, which ->  }
                    builder.show()
                }else if(retornoLogin == "erro"){
                    val builder = AlertDialog.Builder(this@LoginClienteActivity)
                    builder.setTitle("ERRO")
                    builder.setIcon(R.drawable.ic_erro)
                    builder.setMessage("E-mail ou Senha estão incorretos.")
                    builder.setPositiveButton("OK"){dialog, which ->  }
                    builder.show()
                }else{
                    mPreferences = getSharedPreferences("idValue", 0)
                    mEditor = mPreferences!!.edit()
                    mEditor!!.putString("token",retornoLogin)
                    Log.d("TOKEN", retornoLogin)
                    mEditor!!.putString("tipoPerfil","cliente")
                    mEditor!!.putString("email",txt_email_cliente.text.toString())
                    mEditor!!.putString("senha",txt_senha_cliente.text.toString())
                    mEditor!!.commit()
                    val intent = Intent(this, MainActivityFragment::class.java)
                    startActivity(intent)
                    finish()

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
