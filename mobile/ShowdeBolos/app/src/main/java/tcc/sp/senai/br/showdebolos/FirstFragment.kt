package tcc.sp.senai.br.showdebolos

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_first_fragment.*
import tcc.sp.senai.br.showdebolos.adapter.ConfeiteiroHomeAdapter
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog

import android.widget.TextView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.adapter.CategoriaHomeAdapter
import tcc.sp.senai.br.showdebolos.adapter.ProdutoHomeAdapter
import tcc.sp.senai.br.showdebolos.model.*
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import tcc.sp.senai.br.showdebolos.utils.JWTUtils


class  FirstFragment : Fragment(){

    var confeiteiros: List<EnderecoConfeiteiro> = ArrayList()
    var categorias: List<Categoria> = ArrayList()
    var produtos: List<Produto> = ArrayList()
    lateinit var clickBotao: ClickBotao
    var sharedPreferences:SharedPreferences? = null
    var mEditor:SharedPreferences.Editor? = null

    var perfil:List<EnderecoCliente>? = null
    var token:String? = null
    var parts:List<String>? = null
    var idPerfil:String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_first_fragment, container, false)
        val recyclerViewConfeiteiro = view.findViewById(R.id.recyclerViewConfeiteiro) as RecyclerView
        recyclerViewConfeiteiro.layoutManager= LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        val recyclerViewCategoria = view.findViewById(R.id.recyclerViewCategorias) as RecyclerView
        recyclerViewCategoria.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)


        val recyclerViewProduto = view.findViewById(R.id.recyclerViewProdutos) as RecyclerView
        recyclerViewProduto.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        val txt_ver_mais = view.findViewById<TextView>(R.id.txt_ver_mais)


        txt_ver_mais.setOnClickListener {

            clickBotao.clickBotao()

        }


        sharedPreferences = this.activity!!.getSharedPreferences("idValue",0)
        token = sharedPreferences!!.getString("token","")


        parts = token?.split(".")

        var json = parts!![1]

        val tokenDecod = JWTUtils.getJson(json)

        val jsontoken = JSONObject(tokenDecod)


        idPerfil  = jsontoken.getString("codUsuario")

        mEditor = sharedPreferences!!.edit()
        mEditor!!.putString("codUsuario",idPerfil)
        mEditor!!.commit()

        Log.d("COD_CLIENTE", idPerfil.toString())
        Log.d("TOKEN", token.toString())


        carregarRecyclerView()

        val swipeRefreshLayout:SwipeRefreshLayout = view.findViewById(R.id.swipe_home)
        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.escuro))
        swipeRefreshLayout.setOnRefreshListener {

            carregarRecyclerView()
            swipeRefreshLayout.isRefreshing = false

        }


        return view

    }

    override fun onResume() {
        super.onResume()

        val tipoPerfil = sharedPreferences!!.getString("tipoPerfil", "")

        if(tipoPerfil == "cliente"){
            cadastrarEnderecoCliente(txt_view_cidade_cliente)
        }


    }

    fun cadastrarEnderecoCliente(txtCidade:TextView){


        val callPerfil = ApiConfig.getEnderecoCliente().buscarEnderecoCliente(this!!.token!!, this!!.idPerfil!!)

        callPerfil.enqueue(object : retrofit2.Callback<List<EnderecoCliente>>{
            override fun onFailure(call: Call<List<EnderecoCliente>>?, t: Throwable?) {


                Log.d("ERRO_ENDERECO_CLIENTE", t!!.message)
            }

            override fun onResponse(call: Call<List<EnderecoCliente>>?, response: Response<List<EnderecoCliente>>?) {
                perfil = response!!.body()

                Log.d("LIHAS", response.raw().toString())

                if(0 == perfil!!.size){
                    val builder = android.app.AlertDialog.Builder(this@FirstFragment.context)
                    builder.setTitle("Cadastre um endereço")
//                    builder.setIcon(R.drawable.ic_erro)
                    builder.setMessage("Percebemos que você ainda não cadastrou nenhum endereço. Por favor, nos informe um endereço")
                    builder.setPositiveButton("Cadastrar"){dialog, which ->
                        val cadastrarEndereco = Intent(this@FirstFragment.context, CadastroEnderecoClienteActivity::class.java)
                        startActivity(cadastrarEndereco)
                    }
                    builder.show()
                } else {
                    for(i in 0 until perfil!!.size){
                        txtCidade.text = perfil!![i].endereco.cidade.cidade
                    }
                }



                Log.d("ENDERECO_CLIENTE", perfil!!.toString())
            }
        })

    }


    fun CarregarConfeiteiroHome(confeiteiros: List<EnderecoConfeiteiro>?){

        this.confeiteiros = confeiteiros!!

        val confeiteiroHomeAdapter = ConfeiteiroHomeAdapter(confeiteiros, context ,object : ConfeiteiroHomeAdapter.ConfeiteiroOnlickListener{
            override fun onClickConfeiteiro(view: View, index: Int) {
                val c = confeiteiros.get(index)
                AlertDialog.Builder(context!!)
                        .setTitle(c.confeiteiro.nome)
                        .setMessage(c.confeiteiro.sobrenome)
                        .show()
            }

        })

        recyclerViewConfeiteiro.adapter = confeiteiroHomeAdapter

    }

    fun CarregarCategoriaHome(categorias: List<Categoria>?){

        this.categorias = categorias!!

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

    fun CarregarProdutosHome(produtos: List<Produto>?){

        this.produtos = produtos!!

        val produtoHomeAdapter = ProdutoHomeAdapter(produtos, context!!)

        recyclerViewProdutos.adapter = produtoHomeAdapter

    }

    fun carregarRecyclerView(){



            val callConfeiteiro = ApiConfig.getConfeiteiroService()!!.buscarConfeiteiros(sharedPreferences!!.getString("token", ""))


            callConfeiteiro.enqueue(object : Callback<List<EnderecoConfeiteiro>>{

                override fun onResponse(call: Call<List<EnderecoConfeiteiro>>, response: Response<List<EnderecoConfeiteiro>>?) {

                    CarregarConfeiteiroHome(confeiteiros = response!!.body())
                    Log.d("LIHAS", response.raw().toString())

                    Log.i("Retrofit222", "fgfgfgf")
                }

                override fun onFailure(call: Call<List<EnderecoConfeiteiro>>?, t: Throwable?) {
                    Log.i("Retrofit33333", t?.message)
                }

            })

            val callCategoria = ApiConfig.getCategoriaService()!!.buscarCategoria()

            callCategoria.enqueue(object : Callback<List<Categoria>>{

                override fun onResponse(call: Call<List<Categoria>>, response: Response<List<Categoria>>?) {

                    CarregarCategoriaHome(categorias = response!!.body())
                    Log.d("LIHAS", response.raw().toString())

                    Log.i("Retrofit222", "fgfgfgf")
                }

                override fun onFailure(call: Call<List<Categoria>>?, t: Throwable?) {
                    Log.i("Retrofit", t?.message)
                }

            })

            val callProduto = ApiConfig.getProdutoConfeiteiroService()!!.buscarProduto(sharedPreferences!!.getString("token", ""))

            callProduto.enqueue(object : Callback<List<Produto>>{

                override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>?) {
    //

                    CarregarProdutosHome(produtos = response!!.body())
                    Log.d("LIHAS", response.raw().toString())


                    Log.i("Retrofit222", "fgfgfgf")
                }

                override fun onFailure(call: Call<List<Produto>>?, t: Throwable?) {
                    Log.i("Retrofit", t?.message)
                }

            })


    }

    interface ClickBotao{
        fun clickBotao()
    }

}