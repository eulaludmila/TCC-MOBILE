package tcc.sp.senai.br.showdebolos

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_carrinho_fragment.*
import kotlinx.android.synthetic.main.activity_configuracoes_fragment.*
import kotlinx.android.synthetic.main.activity_perfil_confeiteiro.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.adapter.ProdutoCarrinhoAdapter
import tcc.sp.senai.br.showdebolos.dao.ProdutoDAO
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.showdebolos.model.EnderecoConfeiteiro
import tcc.sp.senai.br.showdebolos.model.Pedido
import tcc.sp.senai.br.showdebolos.model.ProdutoDTO
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import java.io.Serializable
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CarrinhoFragment : Fragment() {

    var sharedPreferences:SharedPreferences? = null
    var cliente:Cliente? = null
    var codConfeiteiro: Int = 0

    @SuppressLint("WrongConstant")
    @RequiresApi(28)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_carrinho_fragment, container, false)
        val toolbar = view.findViewById(R.id.toolbar) as android.support.v7.widget.Toolbar

        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            toolbar.textAlignment = View.TEXT_ALIGNMENT_CENTER
            (activity as AppCompatActivity).title = "Carrinho"
        }

        sharedPreferences = this.activity!!.getSharedPreferences("idValue",0)



        val recyclerViewCarrinho: RecyclerView = view.findViewById(R.id.recyclerViewCarrinho2) as RecyclerView
        recyclerViewCarrinho.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false)

        val btnConfirmarPedido = view.findViewById<Button>(R.id.btn_confirmar_pedido)

        val txtNomeConfeiteiro = view.findViewById<TextView>(R.id.txt_nome_confeiteiro_carrinho)

        val txtEnderecoConfeiteiro = view.findViewById<TextView>(R.id.txt_endereco_confeiteiro_carrinho)

        val layoutInfoConfeiteiro = view.findViewById<LinearLayout>(R.id.layout_info_confeiteiro)

        val layoutCarrinhoVazio = view.findViewById<LinearLayout>(R.id.info_carrinho_vazio)


        val dao = ProdutoDAO(context!!)

        var produtosCarrinho = dao.getProdutos()

        val produtoCarrinhoAdapter = ProdutoCarrinhoAdapter(produtosCarrinho, context!!)

        var total = 0.0
        var codConfeiteiro = 0

        for (i in 0 until produtosCarrinho.size){


            total += produtosCarrinho[i].preco

            codConfeiteiro = produtosCarrinho[i].confeiteiro


        }

        if(total == 0.0){
            layoutCarrinhoVazio.visibility = 1
        } else {


            layoutInfoConfeiteiro.visibility = 1

            val callConfeiteiro = ApiConfig.getConfeiteiroService().buscarConfeiteiro(sharedPreferences!!.getString("token", ""), codConfeiteiro.toString() )

            callConfeiteiro.enqueue(object : Callback<EnderecoConfeiteiro> {

                override fun onResponse(call: Call<EnderecoConfeiteiro>, response: Response<EnderecoConfeiteiro>) {

                    val confeiteiro =  response.body()!!

                    codConfeiteiro = confeiteiro.confeiteiro.codConfeiteiro

                    txtNomeConfeiteiro.text = "${confeiteiro.confeiteiro.nome} ${confeiteiro.confeiteiro.sobrenome}"
                    txtEnderecoConfeiteiro.text = "${confeiteiro.endereco.endereco}, nº${confeiteiro.endereco.numero} - ${confeiteiro.endereco.bairro}"
                    Picasso.with(img_foto_confeiteiro_carrinho.context).load("http://3.232.178.219${confeiteiro.confeiteiro.foto}").into(img_foto_confeiteiro_carrinho)


                }

                override fun onFailure(call: Call<EnderecoConfeiteiro>?, t: Throwable?) {
                    Log.i("Retrofit", t?.message)
                }

            })
        }





        btnConfirmarPedido.text = "CONFIRMAR     R$ ${total}"

        recyclerViewCarrinho.adapter = produtoCarrinhoAdapter

        btnConfirmarPedido.setOnClickListener {

            val abrirPagamento = Intent(context, DataEntregaActivity::class.java)
            abrirPagamento.putExtra("total",total)
            abrirPagamento.putExtra("produtos",produtosCarrinho as Serializable)
            abrirPagamento.putExtra("confeiteiro", codConfeiteiro)
            startActivity(abrirPagamento)
        }


        toolbar.setNavigationOnClickListener {


            val fragmentManager: FragmentManager = activity!!.supportFragmentManager

            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.layout_fragment, FirstFragment()).commit() }

        return view
    }


}
