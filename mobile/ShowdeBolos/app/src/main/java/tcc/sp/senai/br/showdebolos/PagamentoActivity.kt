package tcc.sp.senai.br.showdebolos

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pagamento.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.dao.ProdutoDAO
import tcc.sp.senai.br.showdebolos.model.*
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import tcc.sp.senai.br.showdebolos.services.ItemPedidoService
import tcc.sp.senai.br.showdebolos.tasks.PagamentoTasks
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("ByteOrderMark")
class PagamentoActivity : AppCompatActivity() {

    var mPreferences: SharedPreferences? = null
    var cliente: EnderecoCliente? = null
    var confeiteiro: EnderecoConfeiteiro? = null
    var aprovacao:Char = 'E'
    var itemPedidoService:ItemPedidoService? = null

    @RequiresApi(28)
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


        val produtos = intent.getSerializableExtra("produtos") as List<ProdutoDTO>

        Log.d("produtos3333", produtos.toString())


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

    fun fazerPedido(itemPedido: String, token: String){



        val itemBody = RequestBody.create(MediaType.parse("application/json"), itemPedido)
        val call = itemPedidoService!!.fazerPedido(itemBody, token)


        call.enqueue(object: Callback<ItemPedido>{
            override fun onFailure(call: Call<ItemPedido>?, t: Throwable?) {
//                Toast.makeText(this@PagamentoActivity,"Deu errado",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ItemPedido>?, response: Response<ItemPedido>?) {
//                Toast.makeText(this@PagamentoActivity,"Deu certo",Toast.LENGTH_LONG).show()

                Log.d("pedido2222222", response!!.body().toString())

            }

        })

    }



    @TargetApi(28)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.confirmar -> {

                if(validar()){
                    val biometricPrompt: BiometricPrompt = BiometricPrompt.Builder(this)
                            .setTitle("Finalizar Pedido")
                            .setDescription("Para finalizar o pedido faça autenticação com sua biometria.")
                            .setNegativeButton("Cancelar", this.getMainExecutor(),
                                    DialogInterface.OnClickListener { dialog, which -> })
                            .build()

                    biometricPrompt.authenticate(getCancellationSignal(), mainExecutor,
                            getAuthenticationCallback())
                }



            }


            else -> {
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun validar():Boolean{

        var validado = true

        if(txt_numero_cartao.text.toString().isEmpty()){
            layout_txt_numero_cartao.isErrorEnabled = true
            layout_txt_numero_cartao.error = resources.getText(0,"Digite o numero do cartão")
            validado = false
        }else if(txt_numero_cartao.length() < 16 ){
            layout_txt_numero_cartao.isErrorEnabled = true
            layout_txt_numero_cartao.error = resources.getText(0, "Digite o numero do cartão corretamente")
            validado = false
        }else{
            layout_txt_numero_cartao.isErrorEnabled = false
        }

        if(txt_nome_titular.text.toString().isEmpty()){
            layout_txt_nome_titular.isErrorEnabled = true
            layout_txt_nome_titular.error = resources.getText(0,"Digite o nome do titular")
            validado = false
        }else{
            layout_txt_nome_titular.isErrorEnabled = false
        }

        if(txt_data_vencimento.text.toString().isEmpty()){
            layout_txt_data_vencimento.isErrorEnabled = true
            layout_txt_data_vencimento.error = resources.getText(0,"Digite a data de vencimento")
            validado = false
        }else if(txt_data_vencimento.length() < 5 ){
            layout_txt_data_vencimento.isErrorEnabled = true
            layout_txt_data_vencimento.error = resources.getText(0, "Digite a data corretamente")
            validado = false
        }else{
            layout_txt_data_vencimento.isErrorEnabled = false
        }

        if(txt_codigo.text.toString().isEmpty()){
            layout_txt_codigo.isErrorEnabled = true
            layout_txt_codigo.error = resources.getText(0,"Digite a data de vencimento")
            validado = false
        }else if(txt_codigo.length() < 3 ){
            layout_txt_codigo.isErrorEnabled = true
            layout_txt_codigo.error = resources.getText(0, "Digite a data corretamente")
            validado = false
        }else{
            layout_txt_codigo.isErrorEnabled = false
        }

        if(txt_cpf_titular.text.toString().isEmpty()){
            layout_txt_cpf_titular.isErrorEnabled = true
            layout_txt_cpf_titular.error = resources.getText(0,"Digite o cpf do titular")
            validado = false
        }else if(txt_cpf_titular.length() < 11 ){
            layout_txt_cpf_titular.isErrorEnabled = true
            layout_txt_cpf_titular.error = resources.getText(0, "Digite o cpf corretamente")
            validado = false
        }else{
            layout_txt_cpf_titular.isErrorEnabled = false
        }

        return validado

    }

    @RequiresApi(28)
    fun fazerPedido(){

        val total = intent.getSerializableExtra("total") as Double

        val cartao = Cartao(txt_numero_cartao.text.toString(),txt_codigo.text.toString(),txt_data_vencimento.text.toString(),txt_nome_titular.text.toString(),txt_cpf_titular.text.toString())

        val pagamento = PagamentoTasks(cliente!!,confeiteiro!!,total.toString(),cartao)

        val getData = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val dataAtual = getData.format(Date())

        val data = intent.getStringExtra("data") as String
        val hora = intent.getStringExtra("hora") as String
        val observacao = intent.getStringExtra("observacao") as String

        var valida = true

        pagamento.execute()

        val retornoPagamento = pagamento.get() as String

        if(retornoPagamento == "authorized"){
            aprovacao = 'A'
        }else if(retornoPagamento == "refused"){
            aprovacao = 'R'
        }else if(retornoPagamento == "E"){

            valida = false

        }

        if(valida){


            val itensPedido = arrayListOf<ItemPedido>()

            val produtos = intent.getSerializableExtra("produtos") as List<ProdutoDTO>

            Log.d("produtos3333", produtos.toString())

            val pedido = Pedido(0,total,dataAtual, "${data} $hora:00" ,'C','E', aprovacao ,observacao,"",cliente!!.cliente!!)

            for (i in 0 until produtos.size){

                val itemPedido = ItemPedido(0 , produtos[i], produtos[i].quantidade,produtos[i].preco, pedido)

                itensPedido.add(itemPedido)

            }

            val gson = Gson()
            val json = gson.toJson(itensPedido)

            Log.d("produtos4444", json.toString())

            fazerPedido(json,mPreferences!!.getString("token",""))

            if(aprovacao == 'A'){


                val dao = ProdutoDAO(this)
                dao.excluirTodos()
                val intent = Intent(this, SucessoPedidoActivity::class.java)
                startActivity(intent)
                finish()


            }else if(aprovacao == 'R'){

                val intent = Intent(this, RecusadoPedidoActivity::class.java)
                startActivity(intent)
                finish()


            }
        }else{

            carregando_pagamento.visibility = View.INVISIBLE

            val builder = AlertDialog.Builder(this@PagamentoActivity)
            builder.setTitle("ERRO")
            builder.setIcon(R.drawable.ic_erro)
            builder.setMessage("Erro ao executar transação\nVerifique os seus dados inseridos")
            builder.setPositiveButton("OK"){dialog, which ->

            }
            builder.show()

        }


    }


    private fun getCancellationSignal(): CancellationSignal {

        val cancellationSignal = CancellationSignal()
        cancellationSignal!!.setOnCancelListener(object : CancellationSignal.OnCancelListener {
            override fun onCancel() {
                Toast.makeText(this@PagamentoActivity, "Cancelled via signal", Toast.LENGTH_LONG).show()

            }
        })
        return cancellationSignal!!
    }

    @TargetApi(28)
    private fun getAuthenticationCallback(): BiometricPrompt.AuthenticationCallback {

        return object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int,
                                               errString: CharSequence) {

                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationHelp(helpCode: Int,
                                              helpString: CharSequence) {
                super.onAuthenticationHelp(helpCode, helpString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                carregando_pagamento.visibility = View.VISIBLE
                fazerPedido()
                super.onAuthenticationSucceeded(result)
            }
        }
    }

}
