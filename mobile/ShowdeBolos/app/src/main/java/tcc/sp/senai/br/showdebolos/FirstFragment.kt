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
import android.support.v4.app.ActivityCompat.finishAffinity
import android.support.v7.app.AlertDialog

import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import org.jetbrains.anko.doFromSdk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.adapter.CategoriaHomeAdapter
import tcc.sp.senai.br.showdebolos.R.id.recyclerViewConfeiteiro
import tcc.sp.senai.br.showdebolos.model.*
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import tcc.sp.senai.br.showdebolos.services.RetrofitClient


class  FirstFragment : Fragment() {
    var confeiteiros: List<ConfeiteiroDTO> = ArrayList()
    var categorias: List<Categoria> = ArrayList()
    lateinit var clickBotao: ClickBotao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_first_fragment, container, false)
        val recyclerViewConfeiteiro = view.findViewById(R.id.recyclerViewConfeiteiro) as RecyclerView
        recyclerViewConfeiteiro.layoutManager= LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
//        recyclerViewConfeiteiro.adapter = ConfeiteiroHomeAdapter(confeiteiros, requireContext(), object:ConfeiteiroHomeAdapter.ConfeiteiroOnlickListener{
//            override fun onClickConfeiteiro(view: View, index: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        })

        val recyclerViewCategoria = view.findViewById(R.id.recyclerViewCategorias) as RecyclerView
        recyclerViewCategoria.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        recyclerViewCategoria.adapter = CategoriaHomeAdapter(categorias, requireContext(), object: CategoriaHomeAdapter.CategoriaOnlickListener{
//            override fun onClickCategoria(view: View, index: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//
//                Toast.makeText(context,"clickcategoriua",Toast.LENGTH_SHORT)
//            }

//        })

        val txt_ver_mais = view.findViewById<TextView>(R.id.txt_ver_mais)

        txt_ver_mais.setOnClickListener {

            val intent = Intent(context, VisualizarProdutoActivity::class.java)
            startActivity(intent)

        }


        val callConfeiteiro = ApiConfig.getConfeiteiroService()!!.buscarConfeiteiros()

        callConfeiteiro.enqueue(object : Callback<List<ConfeiteiroDTO>>{

            override fun onResponse(call: Call<List<ConfeiteiroDTO>>, response: Response<List<ConfeiteiroDTO>>) {
//
              CarregarConfeiteiroHome(confeiteiros = response.body()!!)
                Log.i("Retrofit222", "fgfgfgf")
            }

            override fun onFailure(call: Call<List<ConfeiteiroDTO>>?, t: Throwable?) {
                Log.i("Retrofit", t?.message)
            }

        })

        val callCategoria = ApiConfig.getCategoriaService()!!.buscarCategoria()

        callCategoria.enqueue(object : Callback<List<Categoria>>{

            override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>) {
//
                CarregarCategoriaHome(categorias = response.body()!!)
                Log.i("Retrofit222", "fgfgfgf")
            }

            override fun onFailure(call: Call<List<Categoria>>?, t: Throwable?) {
                Log.i("Retrofit", t?.message)
            }

        })


        return view


    }


    fun CarregarConfeiteiroHome(confeiteiros: List<ConfeiteiroDTO> ){

        this.confeiteiros = confeiteiros

        val confeiteiroHomeAdapter = ConfeiteiroHomeAdapter(confeiteiros, context ,object : ConfeiteiroHomeAdapter.ConfeiteiroOnlickListener{
            override fun onClickConfeiteiro(view: View, index: Int) {
                val c = confeiteiros.get(index)
                AlertDialog.Builder(context!!)
                        .setTitle(c.nome)
                        .setMessage(c.sobrenome)
                        .show()
            }

        })

        recyclerViewConfeiteiro.adapter = confeiteiroHomeAdapter

    }

    fun CarregarCategoriaHome(categorias: List<Categoria> ){

        this.categorias = categorias

        val categoriaHomeAdapter = CategoriaHomeAdapter(categorias, context!! ,object : CategoriaHomeAdapter.CategoriaOnlickListener{
            override fun onClickCategoria(view: View, index: Int) {
                val c = categorias.get(index)
                AlertDialog.Builder(context!!)
                        .setTitle(c.categoria)
                        .show()
            }

        })

        recyclerViewCategorias.adapter = categoriaHomeAdapter

    }

    interface ClickBotao{
        fun clickBotao()
    }

}