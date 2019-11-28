package tcc.sp.senai.br.showdebolos

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_circular_reveal.*
import kotlinx.android.synthetic.main.activity_login_cliente.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import tcc.sp.senai.br.showdebolos.model.Celular
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import tcc.sp.senai.br.showdebolos.tasks.LoginClienteTasks
import tcc.sp.senai.br.showdebolos.tasks.LoginConfeiteiroTasks
import tcc.sp.senai.br.showdebolos.utils.JWTUtils

class CircularReveal : AppCompatActivity() {


    var mPreferences: SharedPreferences? = null
    var mEditor:SharedPreferences.Editor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0,0)
        setContentView(R.layout.activity_circular_reveal)


        if (savedInstanceState == null) {
            root_layout.setVisibility(View.INVISIBLE)

            val viewTreeObserver = root_layout.getViewTreeObserver()
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        circularRevealActivity()
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            root_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this)
                        } else {
                            root_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                        }
                    }
                })
            }
        }

        Handler().postDelayed({
            checkSharedPreferences()
        }, 200)

    }

    private fun circularRevealActivity() {

        val cx = root_layout.getWidth() / 2
        val cy = root_layout.getHeight() / 2

        val finalRadius = Math.max(root_layout.getWidth(), root_layout.getHeight())

        // create the animator for this view (the start radius is zero)
        val circularReveal = ViewAnimationUtils.createCircularReveal(root_layout, cx, cy, 0f, finalRadius.toFloat())
        circularReveal.duration = 1000

        // make the view visible and start the animation
        root_layout.setVisibility(View.VISIBLE)
        circularReveal.start()
    }

    fun checkSharedPreferences(){

        mPreferences = getSharedPreferences("idValue",0)
        val email = mPreferences!!.getString("email", "")
        val senha = mPreferences!!.getString("senha", "")
        val tipoPerfil = mPreferences!!.getString("tipoPerfil", "")

        if(email != "" && senha != ""){
            if(tipoPerfil == "cliente"){
                val cliente = Cliente(0,"","","","",email,senha, Celular(0,""),"","")
                val loginCliente = LoginClienteTasks(cliente, this)
                loginCliente.execute()
                val retornoLogin = loginCliente.get()
                mEditor = mPreferences!!.edit()
                mEditor!!.putString("token",retornoLogin)
                mEditor!!.commit()
                val intent = Intent(this, MainActivityFragment::class.java)
                startActivity(intent)
                finish()
            }else if(tipoPerfil == "confeiteiro"){
                val confeiteiro = Confeiteiro(0,"","","","",email,senha, Celular(0,""),"","",0.0)
                val loginConfeiteiro = LoginConfeiteiroTasks(confeiteiro, this)
                loginConfeiteiro.execute()
                val retornoLogin = loginConfeiteiro.get()
                mEditor = mPreferences!!.edit()
                mEditor!!.putString("token",retornoLogin)
                mEditor!!.commit()
                val intent = Intent(this, MainActivityFragment::class.java)
                startActivity(intent)
                finish()
            }
        }else{
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            overridePendingTransition(0,0)
            finish()
        }

    }

}
