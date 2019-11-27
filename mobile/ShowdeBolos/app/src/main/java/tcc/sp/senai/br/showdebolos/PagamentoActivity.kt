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
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_configuracoes_fragment.*
import kotlinx.android.synthetic.main.activity_pagamento.*
import retrofit2.Call
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.model.*
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import tcc.sp.senai.br.showdebolos.tasks.PagamentoTasks


class PagamentoActivity : AppCompatActivity() {

    var mPreferences: SharedPreferences? = null
    var cliente: EnderecoCliente? = null
    var confeiteiro: EnderecoConfeiteiro? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento)

        mPreferences = this!!.getSharedPreferences("idValue", 0)
        val token = mPreferences!!.getString("token","")
        val idPerfil = mPreferences!!.getString("codCliente","")

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

        val callCliente = ApiConfig.getClienteService().buscarEnderecoCliente(token, idPerfil)

        callCliente.enqueue(object : retrofit2.Callback<EnderecoCliente>{
            override fun onFailure(call: Call<EnderecoCliente>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<EnderecoCliente>?, response: Response<EnderecoCliente>?) {
                val perfil = response!!.body()

                cliente = EnderecoCliente(perfil!!.codEnderecoConfeiteiro,perfil.cliente,perfil.endereco)
            }

        })


        val callConfeiteiro = ApiConfig.getConfeiteiroService().buscarConfeiteiro(token, idPerfil)

        callConfeiteiro.enqueue(object : retrofit2.Callback<EnderecoConfeiteiro>{
            override fun onFailure(call: Call<EnderecoConfeiteiro>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<EnderecoConfeiteiro>?, response: Response<EnderecoConfeiteiro>?) {
                val perfil = response!!.body()

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

        txt_numero_cartao.addTextChangedListener(Mask.mask("#### #### #### ####", txt_numero_cartao))
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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.confirmar -> {

                val total = intent.getSerializableExtra("confeiteiro") as Int

                val cartao = Cartao(txt_numero_cartao.text.toString(),txt_codigo.text.toString(),txt_data_vencimento.text.toString(),txt_nome_titular.text.toString(),txt_cpf_titular.text.toString())

//                val pagamento = PagamentoTasks(cliente!!,confeiteiro!!,total,)


                finish()

            }


            else -> {
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
