package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import br.senai.sp.estacionamento.tasks.CarregarEnderecoTasks
import kotlinx.android.synthetic.main.activity_cadastro_endereco_cliente.*
import tcc.sp.senai.br.showdebolos.model.*
import tcc.sp.senai.br.showdebolos.tasks.CadastrarEnderecoTasks
import java.util.concurrent.ExecutionException

class CadastroEnderecoConfeiteiro : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_endereco_cliente)

        txt_cep_cliente.addTextChangedListener(Mask.mask("#####-###", txt_cep_cliente))

        val estado = Estado(0,txt_uf_cliente.text.toString())

        val cidade = Cidade(0,txt_cidade_cliente.text.toString(), estado)

        val endereco = Endereco(0, txt_cep_cliente.text.toString(),txt_rua_cliente.text.toString(),txt_bairro_cliente.text.toString(),cidade)

        val intent = getIntent()
        val confeiteiro: Confeiteiro = intent.getSerializableExtra("confeiteiro") as Confeiteiro

        val enderecoConfeiteiro = EnderecoConfeiteiro(0, confeiteiro, endereco)


        val cadastrarEndereco = CadastrarEnderecoTasks(endereco)
        cadastrarEndereco.execute()

        val retornoEndereco = cadastrarEndereco.get() as Endereco

        txt_cep_cliente.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                //Criação da variavel com o valor do cep digitado
                val cep = txt_cep_cliente.text.toString()

                //Inseção do cep na classe endereço
                endereco.cep = cep

                //Verificação se foram feito todos os digitos do cep
                if (txt_cep_cliente.length() == 9){


                    val carregarEndereco = CarregarEnderecoTasks(endereco, this@CadastroEnderecoConfeiteiro)
                    //Executando a função que traz o JSON do viacep
                    carregarEndereco.execute()

                    try {

                        val endereco = carregarEndereco.get() as Endereco
                        //Colocando os dados do JSON em suas respectivas caixas de texto
                        txt_cidade_cliente.setText(endereco.codCidade.cidade)
                        txt_rua_cliente.setText(endereco.endereco)
                        txt_bairro_cliente.setText(endereco.bairro)
                        txt_uf_cliente.setText(endereco.codCidade.codEstado.uf)


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
