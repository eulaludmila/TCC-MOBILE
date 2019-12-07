package tcc.sp.senai.br.showdebolos


import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_configuracoes_fragment.*
import kotlinx.android.synthetic.main.activity_detalhes_pedido.*
import kotlinx.android.synthetic.main.activity_visualizar_produto.*
import retrofit2.Call
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.adapter.ProdutoDetalheAdapter
import tcc.sp.senai.br.showdebolos.model.EnderecoConfeiteiro
import tcc.sp.senai.br.showdebolos.model.ItemPedido
import tcc.sp.senai.br.showdebolos.model.Pedido
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import javax.security.auth.callback.Callback


class DetalhePedidoActivity : AppCompatActivity() {

    private var produtos: List<ItemPedido> = ArrayList()
    var sharedPreferences: SharedPreferences? = null
    var codConfeiteiro = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_pedido)

        sharedPreferences = this.getSharedPreferences("idValue",0)

        val pedido = intent.getSerializableExtra("pedido") as Pedido
        val token = sharedPreferences!!.getString("token", "")


        when {
            pedido.producao == null -> txt_producao_pedido.text = "Não iniciado"
            pedido.producao == "E" -> txt_producao_pedido.text = "Em andamento"
            pedido.producao == "F" -> txt_producao_pedido.text = "Finalizado"
        }

        txt_data_produto_detalhe.text = "Pedido efetuado em ${formatarData(pedido.dataSolicitacao)}"
        txt_data_entrega_detalhe.text = "Data de entrega ${formatarData(pedido.dataEntrega)} ${formatarHora(pedido.dataEntrega)}"

        title = "Pedido Nº ${pedido.codPedido}"

        val recyclerViewDetalhe: RecyclerView = findViewById(R.id.recycler_view_produtos_detalhe)
        recyclerViewDetalhe.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)



        val callItemPedido = ApiConfig.getItemPedido().buscarItensPedido(pedido.codPedido, token)

        callItemPedido.enqueue(object : retrofit2.Callback<List<ItemPedido>>{
            override fun onFailure(call: Call<List<ItemPedido>>?, t: Throwable?) {
                Log.d("erro2222", t!!.message)
            }

            override fun onResponse(call: Call<List<ItemPedido>>?, response: Response<List<ItemPedido>>?) {

                val body = response!!.body()

                CarregarProdutosDetalhe(body!!)

                val callPerfil = ApiConfig.getConfeiteiroService().buscarConfeiteiro(token, body!![0].produto.confeiteiro.toString())

                callPerfil.enqueue(object : retrofit2.Callback<EnderecoConfeiteiro>{
                    override fun onFailure(call: Call<EnderecoConfeiteiro>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<EnderecoConfeiteiro>?, response: Response<EnderecoConfeiteiro>?) {
                        val perfil = response!!.body()

                        txt_confeiteiro_detalhe.text = "Vendido por ${perfil!!.confeiteiro.nome} ${perfil!!.confeiteiro.sobrenome}"
                        txt_cidade_detalhe.text= perfil!!.endereco.cidade.cidade +" - "+ perfil!!.endereco.cidade.estado.uf
                        txt_rua_detalhe.text = perfil!!.endereco.endereco + ", nº" + perfil!!.endereco.numero
                        txt_bairro_detalhe.text = perfil!!.endereco.bairro
                        Picasso.with(img_confeiteiro_detalhe.context).load("http://3.232.178.219${perfil.confeiteiro.foto}").into(img_confeiteiro_detalhe)
                    }
                })

            }

        })

        Log.d("confeiteiro4444", codConfeiteiro)




    }

    fun CarregarProdutosDetalhe(produtos: List<ItemPedido> ){

        this.produtos = produtos

        val produtoDetalheAdapter = ProdutoDetalheAdapter(produtos, this)

        recycler_view_produtos_detalhe.adapter = produtoDetalheAdapter

    }

    fun formatarData(dataBanco: String): String {

        val date = dataBanco.split("-", " ")

        val ano = date[0]
        val mes = date[1]
        val dia = date[2]

        val dataBR = "${dia}/${mes}/${ano}"

        return dataBR

    }


    fun formatarHora(dataBanco: String): String {

        val date = dataBanco.split("-", ":", " ")

        val hora = date[3]
        val minuto = date[4]

        val horaBR = "${hora}:${minuto}"

        return horaBR

    }


}