package tcc.sp.senai.br.showdebolos

import android.content.SharedPreferences
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.util.JsonToken
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_configuracoes_fragment.*
import kotlinx.android.synthetic.main.activity_pagamento.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.model.*
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import tcc.sp.senai.br.showdebolos.services.ItemPedidoService
import tcc.sp.senai.br.showdebolos.tasks.PagamentoTasks
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PagamentoActivity : AppCompatActivity() {

    var mPreferences: SharedPreferences? = null
    var cliente: EnderecoCliente? = null
    var confeiteiro: EnderecoConfeiteiro? = null
    var aprovacao:Char = 'E'
    var itemPedidoService:ItemPedidoService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento)

        itemPedidoService = ApiConfig.getItemPedido()

        mPreferences = this!!.getSharedPreferences("idValue", 0)
        val token = mPreferences!!.getString("token","")
        val idPerfil = mPreferences!!.getString("codUsuario","")
        val codConfeiteiro = intent.getSerializableExtra("confeiteiro") as Int

        try {
            val field = TextInputLayout::class.java.getDeclaredField("defaultStrokeColor")
            field.isAccessible = true
            field.set(layout_txt_nome_titular,ContextCompat.getColor(layout_txt_nome_titular.context, R.color.preto))
            field.set(layout_txt_numero_cartao,ContextCompat.getColor(layout_txt_numero_cartao.context, R.color.preto))
            field.set(layout_txt_data_vencimento,ContextCompat.getColor(layout_txt_data_vencimento.context, R.color.preto))
            field.set(layout_txt_codigo,ContextCompat.getColor(layout_txt_codigo.context, R.color.preto))
            field.set(layout_txt_cpf_titular,ContextCompat.getColor(layout_txt_cpf_titular.context, R.color.preto))
        } catch (e: NoSuchFieldException) {
            Log.w("TAG", "Failed to change box color, item might look wrong")
        } catch (e: IllegalAccessException) {
            Log.w("TAG", "Failed to change box color, item might look wrong")
        }

        val callCliente = ApiConfig.getEnderecoCliente().buscarEnderecoCliente(token, idPerfil)

        callCliente.enqueue(object : retrofit2.Callback<List<EnderecoCliente>>{
            override fun onFailure(call: Call<List<EnderecoCliente>>?, t: Throwable?) {
                Log.d("falha222" , t!!.message)
            }

            override fun onResponse(call: Call<List<EnderecoCliente>>?, response: Response<List<EnderecoCliente>>?) {
                val perfil = response!!.body()

                Log.d("perfil222", perfil.toString())

                cliente = EnderecoCliente(perfil!![0].codEnderecoConfeiteiro,perfil!![0].cliente,perfil!![0].endereco)
            }

        })


        val callConfeiteiro = ApiConfig.getConfeiteiroService().buscarConfeiteiro(token, codConfeiteiro.toString())

        callConfeiteiro.enqueue(object : retrofit2.Callback<EnderecoConfeiteiro>{
            override fun onFailure(call: Call<EnderecoConfeiteiro>?, t: Throwable?) {

                Log.d("falha3333", t!!.message)


            }

            override fun onResponse(call: Call<EnderecoConfeiteiro>?, response: Response<EnderecoConfeiteiro>?) {
                val perfil = response!!.body()

                Log.d("perfil3333", perfil.toString())

                confeiteiro = EnderecoConfeiteiro(perfil!!.codEnderecoConfeiteiro,perfil.confeiteiro,perfil.endereco)

            }
        })


        txt_codigo.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus){
                creditCardView.showBack()
                txt_codigo.addTextChangedListener(object : TextWatcher{
                    override fun afterTextChanged(s: Editable?) {

                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        creditCardView.cvv = txt_codigo.text.toString()
                    }

                })
            }else{
                creditCardView.showFront()
            }

        }

        txt_numero_cartao.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                creditCardView.cardNumber = txt_numero_cartao.text.toString()
            }

        })

        txt_nome_titular.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                creditCardView.cardHolderName = txt_nome_titular.text.toString()
            }

        })

        txt_data_vencimento.addTextChangedListener(Mask.mask("##/##", txt_data_vencimento))
        txt_data_vencimento.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                creditCardView.setCardExpiry(txt_data_vencimento.text.toString())
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val menuInflater = menuInflater

        menuInflater.inflate(R.menu.context_menu, menu)


        return super.onCreateOptionsMenu(menu)
    }

    fun fazerPedido(itemPedido: List<ItemPedido>, token: String){

        val call = itemPedidoService!!.fazerPedido(itemPedido, token)

        call.enqueue(object: Callback<ItemPedido>{
            override fun onFailure(call: Call<ItemPedido>?, t: Throwable?) {
                Toast.makeText(this@PagamentoActivity,"Deu errado",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ItemPedido>?, response: Response<ItemPedido>?) {
                Toast.makeText(this@PagamentoActivity,"Deu certo",Toast.LENGTH_LONG).show()

                Log.d("pedido2222222", response!!.body().toString())

            }

        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.confirmar -> {

                val total = intent.getSerializableExtra("total") as Double

                val cartao = Cartao(txt_numero_cartao.text.toString(),txt_codigo.text.toString(),txt_data_vencimento.text.toString(),txt_nome_titular.text.toString(),txt_cpf_titular.text.toString())

                val pagamento = PagamentoTasks(cliente!!,confeiteiro!!,total.toString(),cartao)

                val data = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dataHora = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())

                val dataAtual = data.format(Date())
                val dataHoraAtual = dataHora.format(Date())


                pagamento.execute()

                val retornoPagamento = pagamento.get() as String

                if(retornoPagamento == "authorized"){
                    aprovacao = 'A'
                }else if(retornoPagamento == "refused"){
                    aprovacao = 'R'
                }else{
                    aprovacao = 'R'
                }

                val itensPedido = arrayListOf<ItemPedido>()

                val produtos = intent.getSerializableExtra("produtos") as List<ProdutoDTO>

                val pedido = Pedido(0,total,dataAtual,dataHoraAtual,'C','N', aprovacao ,"",cliente!!.cliente!!)

                for (i in 0 until produtos.size){

                    val itemPedido = ItemPedido(0 , produtos[i], produtos[i].quantidade,produtos[i].precoTotal, pedido)

                    itensPedido.add(itemPedido)

                }

                val gson = Gson()
                val json = gson.toJson(itensPedido)
//                val produto2 = gson.fromJson<List<ItemPedido>>(json, ItemPedido::class.java)

                fazerPedido(itensPedido,mPreferences!!.getString("token",""))

                Log.d("pagamento222", json.toString())
                Log.d("pagamento222", itensPedido.toString())
                Toast.makeText(this, retornoPagamento , Toast.LENGTH_LONG).show()


                finish()

            }


            else -> {
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
