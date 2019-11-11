package tcc.sp.senai.br.showdebolos

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_perfil_confeiteiro.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.adapter.ProdutoHomeAdapter
import tcc.sp.senai.br.showdebolos.model.EnderecoConfeiteiro
import tcc.sp.senai.br.showdebolos.model.Produto
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import kotlin.collections.ArrayList

class PerfilConfeiteiroActivity : AppCompatActivity() {

    private var produtos: List<Produto> = ArrayList()
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_confeiteiro)

        sharedPreferences = this.getSharedPreferences("idValue",0)

        val confeiteiro = intent.getSerializableExtra("confeiteiro") as EnderecoConfeiteiro
        val recyclerViewPerfilConfeiteiro: RecyclerView = findViewById(R.id.recyclerViewPerfilConfeiteiro)
        recyclerViewPerfilConfeiteiro.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        val callProduto = ApiConfig.getProdutoService()!!.buscarProdutoConfeiteiro(confeiteiro.confeiteiro.codConfeiteiro.toString(),sharedPreferences!!.getString("token", ""))

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

        Log.d("confeiteiro2222222", confeiteiro.toString())

        txt_nome_confeiteiro_perfil.text = confeiteiro.confeiteiro.nome + " " + confeiteiro.confeiteiro.sobrenome
        rt_avaliacao_confeiteiro_perfil.progress = confeiteiro.confeiteiro.avaliacao.toInt()
        txt_cidade.text = confeiteiro.endereco.cidade.cidade
        txt_uf.text = confeiteiro.endereco.cidade.estado.uf
        Picasso.with(img_foto_confeiteiro.context).load("http://3.232.178.219${confeiteiro.confeiteiro.foto}").into(img_foto_confeiteiro)
        Picasso.with(img_foto_confeiteiro.context).load("http://3.232.178.219${confeiteiro.confeiteiro.foto}").into(img_foto_confeiteiro)




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
