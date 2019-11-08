package tcc.sp.senai.br.showdebolos

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_carrinho_fragment.*
import kotlinx.android.synthetic.main.activity_first_fragment.*
import kotlinx.android.synthetic.main.activity_main_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.adapter.ConfeiteiroHomeAdapter
import tcc.sp.senai.br.adapter.ProdutoCarrinhoAdapter
import tcc.sp.senai.br.showdebolos.model.EnderecoConfeiteiro
import tcc.sp.senai.br.showdebolos.model.Produto
import tcc.sp.senai.br.showdebolos.services.ApiConfig

class CarrinhoFragment : Fragment() {

    var sharedPreferences: SharedPreferences? = null
    var produtos: List<Produto> = ArrayList()
    var cod:String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_carrinho_fragment, container, false)
        val toolbar = view.findViewById(R.id.toolbar) as android.support.v7.widget.Toolbar
//        val txt = view.findViewById(R.id.txt) as TextView
        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            toolbar.textAlignment = View.TEXT_ALIGNMENT_CENTER
            (activity as AppCompatActivity).title = "Carrinho"
        }

        sharedPreferences = this.activity!!.getSharedPreferences("idValue",0)

        val recyclerViewCarrinho: RecyclerView = view.findViewById(R.id.recyclerViewCarrinho)
        recyclerViewCarrinho.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)

        val produtosId = sharedPreferences!!.getStringSet("idProduto", mutableSetOf<String>())

        for (i in produtosId.indices){

            val id = produtosId.elementAt(i)

            this.cod = id
        }

        val callProduto = ApiConfig.getProdutoService().buscarProdutoId(cod!!)

        callProduto.enqueue(object : Callback<List<Produto>> {

            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
//
                CarregarProduto(produtos = response.body()!!)
                Log.i("Retrofit222", "fgfgfgf")
            }

            override fun onFailure(call: Call<List<Produto>>?, t: Throwable?) {
                Log.i("Retrofit", t?.message)
            }

        })

        toolbar.setNavigationOnClickListener {


            val fragmentManager: FragmentManager = activity!!.supportFragmentManager

            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.layout_fragment, FirstFragment()).commit() }

        return view
    }

    fun CarregarProduto(produtos: List<Produto> ){

        this.produtos = produtos

        val produtoCarrinhoAdapter = ProdutoCarrinhoAdapter(produtos, context!!)

        recyclerViewCarrinho.adapter = produtoCarrinhoAdapter

    }

}
