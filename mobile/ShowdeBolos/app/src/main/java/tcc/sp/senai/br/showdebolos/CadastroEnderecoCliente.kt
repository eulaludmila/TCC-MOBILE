package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import br.senai.sp.estacionamento.tasks.CarregarEndereco
import kotlinx.android.synthetic.main.activity_cadastro_endereco_cliente.*
import tcc.sp.senai.br.showdebolos.model.Endereco
import java.util.concurrent.ExecutionException

class CadastroEnderecoCliente : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_endereco_cliente)

        txt_cep_cliente.addTextChangedListener(Mask.mask("#####-###", txt_cep_cliente))

        val endereco = Endereco(txt_cep_cliente.text.toString(),txt_rua_cliente.text.toString(),txt_bairro_cliente.text.toString(),txt_cidade_cliente.text.toString(),txt_uf_cliente.text.toString())


        txt_cep_cliente.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                //Criação da variavel com o valor do cep digitado
                val cep = txt_cep_cliente.text.toString()

                //Inseção do cep na classe endereço
                endereco.cep = cep

                //Verificação se foram feito todos os digitos do cep
                if (txt_cep_cliente.length() == 9){


                    val carregarEndereco = CarregarEndereco(endereco, this@CadastroEnderecoCliente)
                    //Executando a função que traz o JSON do viacep
                    carregarEndereco.execute()

                    try {

                        val endereco = carregarEndereco.get() as Endereco
                        //Colocando os dados do JSON em suas respectivas caixas de texto
                        txt_cidade_cliente.setText(endereco.cidade)
                        txt_rua_cliente.setText(endereco.logradouro)
                        txt_bairro_cliente.setText(endereco.bairro)
                        txt_uf_cliente.setText(endereco.uf)


                    }catch (e : ExecutionException){
                        e.printStackTrace()
                    }catch (e: InterruptedException){
                        e.printStackTrace()
                    }

                }


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                txt_cidade_cliente.setText("")
                txt_rua_cliente.setText("")
                txt_bairro_cliente.setText("")
                txt_uf_cliente.setText("")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                txt_cidade_cliente.setText("")
                txt_rua_cliente.setText("")
                txt_bairro_cliente.setText("")
                txt_uf_cliente.setText("")


            }

        })





    }
}
