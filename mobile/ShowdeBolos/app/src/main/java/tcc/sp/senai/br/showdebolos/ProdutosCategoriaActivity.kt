package tcc.sp.senai.br.showdebolos

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import kotlinx.android.synthetic.main.activity_produtos_categoria_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.adapter.ProdutoHomeAdapter
import tcc.sp.senai.br.showdebolos.model.Categoria
import tcc.sp.senai.br.showdebolos.model.Produto
import tcc.sp.senai.br.showdebolos.services.ApiConfig

class ProdutosCategoriaActivity : AppCompatActivity() {

    var produtos: List<Produto> = ArrayList()
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produtos_categoria_activity)
        sharedPreferences = this.getSharedPreferences("idValue",0)


        val recyclerViewProdutosCategoria: RecyclerView = findViewById(R.id.recyclerViewProdutosCategorias)
        recyclerViewProdutosCategoria.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        val categoria = intent.getSerializableExtra("categoria") as Categoria

        val callProduto = ApiConfig.getProdutoConfeiteiroService()!!.buscarProdutoCategoria(categoria.codCategoria.toString(),sharedPreferences!!.getString("token", ""))

        callProduto.enqueue(object : Callback<List<Produto>>{

            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
//
                CarregarProdutosHome(produtos = response.body()!!)
                Log.i("Retrofit222", "fgfgfgf")
            }

            override fun onFailure(call: Call<List<Produto>>?, t: Throwable?) {
                Log.i("Retrofit", t?.message)
            }

        })

        seta_baixo.setOnClickListener {
            finish()
        }


    }

    fun CarregarProdutosHome(produtos: List<Produto> ){

        this.produtos = produtos

        val produtoHomeAdapter = ProdutoHomeAdapter(produtos, this)

        recyclerViewProdutosCategorias.adapter = produtoHomeAdapter

    }



}
