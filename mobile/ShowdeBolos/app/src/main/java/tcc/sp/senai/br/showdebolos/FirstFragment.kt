package tcc.sp.senai.br.showdebolos

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toolbar
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_first_fragment.*
import kotlinx.android.synthetic.main.activity_main_fragment.*
import org.jetbrains.anko.find
import tcc.sp.senai.br.adapter.ConfeiteiroHomeAdapter
import tcc.sp.senai.br.showdebolos.model.Celular
import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import tcc.sp.senai.br.showdebolos.model.ConfeiteiroDTO
import tcc.sp.senai.br.showdebolos.model.Produto
import android.support.v4.app.ActivityCompat.finishAffinity
import android.view.MenuItem


class FirstFragment : Fragment() {


    var confeiteiro: ArrayList<String> = ArrayList()
    lateinit var clickBotao: ClickBotao


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.activity_first_fragment, container, false)
            val recyclerViewConfeiteiro = view.findViewById(R.id.recyclerViewConfeiteiro) as RecyclerView
            val seta32 = view.findViewById(R.id.seta32) as ImageView


            seta32.setOnClickListener {

                clickBotao.clickBotao()

            }

            confeiteiro.add("Eulaaa")
            confeiteiro.add("Eulaaa")
            confeiteiro.add("Eulaaa")
            confeiteiro.add("Eulaaa")
            confeiteiro.add("Kailanyyy")
            confeiteiro.add("Kailanyyy")
            confeiteiro.add("Kailanyyy")
            confeiteiro.add("Kailanyyy")

            /*recyclerViewConfeiteiro.layoutManager= LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            recyclerViewConfeiteiro.adapter = ConfeiteiroHomeAdapter(confeiteiro, requireContext())*/
            return view


    }

    override fun onResume() {
        super.onResume()

        recyclerViewConfeiteiro.layoutManager= LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recyclerViewConfeiteiro.adapter = ConfeiteiroHomeAdapter(confeiteiro, requireContext())
    }

    interface ClickBotao{
        fun clickBotao()
    }

}