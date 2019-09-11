package tcc.sp.senai.br.showdebolos

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



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
