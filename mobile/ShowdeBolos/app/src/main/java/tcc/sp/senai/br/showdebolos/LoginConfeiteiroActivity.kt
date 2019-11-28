package tcc.sp.senai.br.showdebolos

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login_cliente.*

import kotlinx.android.synthetic.main.activity_login_confeiteiro.*
import tcc.sp.senai.br.showdebolos.model.Celular
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import tcc.sp.senai.br.showdebolos.tasks.LoginClienteTasks
import tcc.sp.senai.br.showdebolos.tasks.LoginConfeiteiroTasks


class LoginConfeiteiroActivity : AppCompatActivity(){

    var mPreferences: SharedPreferences? = null
    var mEditor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_confeiteiro)


        txt_cadastro_confeiteiro.setOnClickListener {
            val intent = Intent(this, CadastroConfeiteiroActivity::class.java)
            startActivity(intent)
        }

        btn_entrar_confeiteiro.setOnClickListener {

            val confeiteiro = Confeiteiro(0,"","","","",txt_email_confeiteiro.text.toString(),txt_senha_confeiteiro.text.toString(), Celular(0,""),"","",0.0)


            if(txt_email_confeiteiro.text.toString() != null && txt_senha_confeiteiro.text.toString() != null){

                val loginConfeiteiro = LoginConfeiteiroTasks(confeiteiro, this)
                loginConfeiteiro.execute()
                val retornoLogin = loginConfeiteiro.get()
                if(retornoLogin == "conexao"){
                    val builder = AlertDialog.Builder(this@LoginConfeiteiroActivity)
                    builder.setTitle("ERRO")
                    builder.setIcon(R.drawable.ic_erro)
                    builder.setMessage("Erro ao conectador ao servidor.\"Tente novamente mais tarde.\" ")
                    builder.setPositiveButton("OK"){dialog, which ->  }
                    builder.show()
                }else if(retornoLogin == "erro"){
                    val builder = AlertDialog.Builder(this@LoginConfeiteiroActivity)
                    builder.setTitle("ERRO")
                    builder.setIcon(R.drawable.ic_erro)
                    builder.setMessage("E-mail ou Senha estão incorretos.")
                    builder.setPositiveButton("OK"){dialog, which ->  }
                    builder.show()
                }else{
                    mPreferences = getSharedPreferences("idValue", 0)
                    mEditor = mPreferences!!.edit()
                    mEditor!!.putString("token",retornoLogin)
                    mEditor!!.putString("tipoPerfil","confeiteiro")
                    mEditor!!.putString("email",txt_email_confeiteiro.text.toString())
                    mEditor!!.putString("senha",txt_senha_confeiteiro.text.toString())
                    mEditor!!.commit()
                    val intent = Intent(this, MainActivityFragment::class.java)
                    startActivity(intent)
                    finish()
                }

            }else{
                val builder = AlertDialog.Builder(this@LoginConfeiteiroActivity)
                builder.setTitle("ERRO")
                builder.setIcon(R.drawable.ic_erro)
                builder.setMessage("Digite seu e-mail ou senha.")
                builder.setPositiveButton("OK"){dialog, which ->  }
                builder.show()
            }



        }


    }


}