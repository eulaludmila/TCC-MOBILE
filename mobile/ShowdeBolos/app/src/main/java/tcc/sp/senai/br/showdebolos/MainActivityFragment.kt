package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_fragment.*


class MainActivityFragment : AppCompatActivity()  {

    var confeiteiro: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fragment)


        replaceFragment(FirstFragment())

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
                R.id.home2 -> {

                    replaceFragment(FirstFragment())
                    return@setOnNavigationItemSelectedListener true

//                }R.id.sacola -> {
//
//
//                return@setOnNavigationItemSelectedListener true

                }R.id.andamento -> {
                replaceFragment(SecondFragment())

                return@setOnNavigationItemSelectedListener true

            }R.id.pagamento -> {

                replaceFragment(SecondFragment())

                return@setOnNavigationItemSelectedListener true

            }else ->{
                return@setOnNavigationItemSelectedListener false}


            }

        }


    }



    fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_fragment, fragment)
        fragmentTransaction.commit()

    }

    override fun onBackPressed() {
        if (bottomNavigation.getSelectedItemId() !== R.id.home2) {
            bottomNavigation.setSelectedItemId(R.id.home2)
        } else {
            super.onBackPressed()
        }
    }




}
