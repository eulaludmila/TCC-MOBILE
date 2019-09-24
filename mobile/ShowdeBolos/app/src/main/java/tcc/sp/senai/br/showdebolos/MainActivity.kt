package tcc.sp.senai.br.showdebolos

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



    fun alertaInternet (valor: String){

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage(valor)
        if(valor.length < 35){
            alertDialog.setTitle("ERRO!") // O Titulo da notificação

            alertDialog.setNegativeButton(R.string.nao, { _, _ ->
                finish();

            })
            alertDialog.show()
        }else{
            alertDialog.setTitle("ALERTA!") // O Titulo da notificação

            alertDialog.setPositiveButton(R.string.sim, {_,_ ->

            })

            alertDialog.setNegativeButton(R.string.nao, { _, _ ->
                finish();

            })
            alertDialog.show()
        }
    }

    //função que irá verificar qual o tipo de internet que está utilizando
    fun isConnect(context: Context): Boolean{

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

        //wifi
        val isConnectedWIFI: Boolean = activeNetwork?.type == ConnectivityManager.TYPE_WIFI

        //dados móveis
        val isConnectedDadosMoveis: Boolean = activeNetwork?.type == ConnectivityManager.TYPE_MOBILE

        var mensagem = "";

        //verifica se tem conexão com a internet
        if(cm.activeNetworkInfo == null) {
            mensagem = "Você não esta conectado a internet"
            //Toast.makeText(this,"lkd222", Toast.LENGTH_LONG).show()
            alertaInternet(mensagem);

            //verifica se tem coneão wifi
        }else if (isConnectedWIFI == true){
//            Toast.makeText(this,"lkd", Toast.LENGTH_LONG).show()

            //verifica se tem coneão com dados móveis
        }else if(isConnectedDadosMoveis == true){

            mensagem = "Você está usando dados móveis e está sujeito(a) a cobranças da operadora. Deseja continuar ?"
            alertaInternet(mensagem);

        }

        return false


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val app = applicationContext as MyApp
        if (app.isOk) {
            //do nothing
        } else {
            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            main_activity.startAnimation(animation)
            app.isOk = true
        }

        isConnect(this)

        btn_confeiteiro.setOnClickListener {
            val intent = Intent (this, LoginConfeiteiroActivity::class.java)
           startActivity(intent)
         }

        btn_cliente.setOnClickListener {
            val intent = Intent(this, LoginClienteActivity::class.java)
            startActivity(intent)
        }



    }




}
