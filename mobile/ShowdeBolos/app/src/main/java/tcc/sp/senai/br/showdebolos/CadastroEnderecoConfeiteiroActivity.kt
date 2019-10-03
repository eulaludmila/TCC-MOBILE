package tcc.sp.senai.br.showdebolos

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.senai.sp.estacionamento.tasks.CarregarEnderecoTasks
import kotlinx.android.synthetic.main.activity_cadastro_endereco_confeiteiro.*
import tcc.sp.senai.br.showdebolos.model.*
import tcc.sp.senai.br.showdebolos.tasks.CadastrarEnderecoConfeiteiroTasks
import tcc.sp.senai.br.showdebolos.tasks.CadastrarEnderecoTasks
import java.util.concurrent.ExecutionException

class CadastroEnderecoConfeiteiroActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_endereco_confeiteiro)

        val estado = Estado(0,txt_uf_confeiteiro.text.toString(), "")

        val cidade = Cidade(0,txt_cidade_confeiteiro.text.toString(), estado)

        val endereco = Endereco(0,
                txt_rua_cliente.text.toString(),
                txt_numero_confeiteiro.text.toString(),
                txt_complemento_cliente.text.toString(),
                txt_cep_confeiteiro.text.toString(),
                txt_bairro_confeiteiro.text.toString(),
                cidade)


        txt_cep_confeiteiro.addTextChangedListener(Mask.mask("#####-###", txt_cep_confeiteiro))


        txt_cep_confeiteiro.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                //Criação da variavel com o valor do cep digitado
                val cep = txt_cep_confeiteiro.text.toString()

                //Inseção do cep na classe endereço
                endereco.cep = cep

                //Verificação se foram feito todos os digitos do cep
                if (txt_cep_confeiteiro.length() == 9){

                    val carregarEndereco = CarregarEnderecoTasks(endereco, this@CadastroEnderecoConfeiteiroActivity, carregando)
                    carregando!!.setVisibility(View.VISIBLE)
                    Handler().postDelayed({

                        //Executando a função que traz o JSON do viacep
                        carregarEndereco.execute()


                    try {

                        //Colocando os dados do JSON em suas respectivas caixas de texto
                        val endereco = carregarEndereco.get() as Endereco
                        txt_cidade_confeiteiro.setText(endereco.codCidade.cidade)
                        txt_rua_cliente.setText(endereco.endereco)
                        txt_bairro_confeiteiro.setText(endereco.bairro)
                        txt_uf_confeiteiro.setText(endereco.codCidade.codEstado.uf)


                    }catch (e : ExecutionException){
                        e.printStackTrace()
                    }catch (e: InterruptedException){
                        e.printStackTrace()
                    }
                    }, 100)
                }


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                txt_cidade_confeiteiro.setText("")
                txt_rua_cliente.setText("")
                txt_bairro_confeiteiro.setText("")
                txt_uf_confeiteiro.setText("")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                txt_cidade_confeiteiro.setText("")
                txt_rua_cliente.setText("")
                txt_bairro_confeiteiro.setText("")
                txt_uf_confeiteiro.setText("")


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

                val estado = Estado(0,txt_uf_confeiteiro.text.toString(), "")

                val cidade = Cidade(0,txt_cidade_confeiteiro.text.toString(), estado)

                val endereco = Endereco(0, txt_rua_cliente.text.toString(),txt_numero_confeiteiro.text.toString(),txt_complemento_cliente.text.toString(),txt_cep_confeiteiro.text.toString(),txt_bairro_confeiteiro.text.toString(), cidade)

                val intent = intent
                val confeiteiro: Confeiteiro = intent.getSerializableExtra("confeiteiro") as Confeiteiro


                val cadastroEndereco = CadastrarEnderecoTasks(endereco)
                cadastroEndereco.execute()

                val retornoEndereco = cadastroEndereco.get() as Endereco

                val enderecoConfeiteiro = EnderecoConfeiteiro(0, confeiteiro, retornoEndereco)


                val cadastroEnderecoConfeiteiro = CadastrarEnderecoConfeiteiroTasks(enderecoConfeiteiro)
                cadastroEnderecoConfeiteiro.execute()

            }


            else -> {
            }
        }

        return super.onOptionsItemSelected(item)
    }

}