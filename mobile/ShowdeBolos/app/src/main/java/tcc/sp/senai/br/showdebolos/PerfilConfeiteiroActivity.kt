package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toolbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_first_fragment.*
import kotlinx.android.synthetic.main.activity_perfil_confeiteiro.*
import kotlinx.android.synthetic.main.activity_visualizar_produto.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.adapter.ProdutoHomeAdapter
import tcc.sp.senai.br.showdebolos.model.ConfeiteiroDTO
import tcc.sp.senai.br.showdebolos.model.Produto
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import kotlin.collections.ArrayList

class PerfilConfeiteiroActivity : AppCompatActivity() {

    private var produtos: List<Produto> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_confeiteiro)


        val confeiteiro = intent.getSerializableExtra("confeiteiro") as ConfeiteiroDTO
        val recyclerViewPerfilConfeiteiro: RecyclerView = findViewById(R.id.recyclerViewPerfilConfeiteiro)
        recyclerViewPerfilConfeiteiro.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        val callProduto = ApiConfig.getProdutoService()!!.buscarProdutoConfeiteiro(confeiteiro.codConfeiteiro.toString())

        callProduto.enqueue(object : Callback<List<Produto>> {

            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
//
                CarregarProdutosPerfil(produtos = response.body()!!)
                Log.i("Retrofit222", "fgfgfgf")
            }

            override fun onFailure(call: Call<List<Produto>>?, t: Throwable?) {
                Log.i("Retrofit", t?.message)
            }

        })

        txt_nome_confeiteiro_perfil.text = confeiteiro.nome + " " + confeiteiro.sobrenome
        rt_avaliacao_confeiteiro_perfil.progress = confeiteiro.avaliacao.toInt()
        Picasso.with(img_foto_confeiteiro.context).load("http://54.242.6.253${confeiteiro.foto}").into(img_foto_confeiteiro)
        Picasso.with(img_foto_confeiteiro.context).load("http://54.242.6.253${confeiteiro.foto}").into(img_foto_confeiteiro)




        val toolbar:android.support.v7.widget.Toolbar = findViewById(R.id.toolbar_perfil)

        setSupportActionBar(toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }

    }
    fun CarregarProdutosPerfil(produtos: List<Produto> ){

        this.produtos = produtos

        val produtoHomeAdapter = ProdutoHomeAdapter(produtos, this)

        recyclerViewPerfilConfeiteiro.adapter = produtoHomeAdapter

    }
}
