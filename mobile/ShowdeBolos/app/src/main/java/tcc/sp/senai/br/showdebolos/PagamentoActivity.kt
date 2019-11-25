package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import java.lang.reflect.AccessibleObject.setAccessible
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.cooltechworks.creditcarddesign.CreditCardView
import kotlinx.android.synthetic.main.activity_cadastro_endereco_confeiteiro.*
import kotlinx.android.synthetic.main.activity_pagamento.*


class PagamentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento)

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
}
