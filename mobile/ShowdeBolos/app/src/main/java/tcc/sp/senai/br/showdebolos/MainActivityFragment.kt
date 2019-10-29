package tcc.sp.senai.br.showdebolos

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_first_fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_fragment.*
import java.lang.reflect.Field


class MainActivityFragment : AppCompatActivity(), FirstFragment.ClickBotao  {

    val firstFragment: FirstFragment? = null
    var confeiteiro: ArrayList<String> = ArrayList()

    override fun clickBotao(){
        val intent = Intent(this, TodosConfeiteirosActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fragment)

        openFirst()


        /*val view = inflater.inflate(R.layout.activity_first_fragment, container, false)
        val recyclerViewConfeiteiroHome = view.findViewById(R.id.recyclerViewConfeiteiroHome) as RecyclerView*/

        confeiteiro.add("Eulaaa")
        confeiteiro.add("Eulaaa")
        confeiteiro.add("Eulaaa")
        confeiteiro.add("Eulaaa")
        confeiteiro.add("Kailanyyy")
        confeiteiro.add("Kailanyyy")
        confeiteiro.add("Kailanyyy")
        confeiteiro.add("Kailanyyy")


        bottomNavigation.setOnNavigationItemSelectedListener {menuItem ->
            val view: View

            when(menuItem.itemId){
                R.id.inicio -> {

                    openFirst()

                    return@setOnNavigationItemSelectedListener true

                }R.id.carrinho -> {
                replaceFragment(CarrinhoFragment())

                return@setOnNavigationItemSelectedListener true

                }R.id.andamento -> {
                replaceFragment(AndamentoFragment())

                return@setOnNavigationItemSelectedListener true

            }R.id.pagamento -> {

                replaceFragment(PagamentoFragment())

                return@setOnNavigationItemSelectedListener true

            }else ->{
                return@setOnNavigationItemSelectedListener false}


            }

        }


    }






    fun openFirst(){
        var f = FirstFragment()
        f.clickBotao = this

        replaceFragment(f)
    }

    fun replaceFragment(fragment: Fragment){
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.layout_fragment, fragment)
            fragmentTransaction.commit()


    }

    override fun onBackPressed() {
        if (bottomNavigation.selectedItemId !== R.id.inicio) {
            bottomNavigation.selectedItemId = R.id.inicio
        } else {
            super.onBackPressed()
        }
    }




}
