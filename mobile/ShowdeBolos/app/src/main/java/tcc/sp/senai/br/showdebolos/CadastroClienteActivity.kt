package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_cadastro_cliente.*
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import org.json.JSONStringer
import tcc.sp.senai.br.showdebolos.model.Celular
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.util.Verificacao
import java.net.HttpURLConnection
import java.net.URL
import java.io.PrintStream
import java.util.*
import android.R.attr.key




class CadastroClienteActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cliente)

        txt_cpf_cliente.addTextChangedListener(Mask.mask("###.###.###-##", txt_cpf_cliente))
        txt_celular_cliente.addTextChangedListener(Mask.mask("(##) #####-####", txt_celular_cliente))
        txt_dt_nascimento_cliente.addTextChangedListener(Mask.mask("####-##-##", txt_dt_nascimento_cliente))



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val menuInflater = menuInflater

        menuInflater.inflate(R.menu.context_menu, menu)


        return super.onCreateOptionsMenu(menu)
    }

    //hora que o menu é selecionado
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.getItemId()) {
            R.id.confirmar -> {


                cadastrarCelular()

                //Toast.makeText(this, .toString(), Toast.LENGTH_LONG).show()
//                finish()
            }


            else -> {
            }
        }//abrir o banco
        //query de insert
        //fechar o banco
        return super.onOptionsItemSelected(item)
    }


    fun cadastrarCelular() {

        val txtCelular = findViewById<TextView>(R.id.txt_celular_cliente)

        val celular = Celular(0, txtCelular.text.toString())

        val url = URL("http://10.107.144.10:8080/celular")

        doAsync {

            val jsCelular = JSONStringer()

            jsCelular.`object`()
            jsCelular.key("celular").value(celular.celular)
            jsCelular.endObject()

            val conexao = url.openConnection() as HttpURLConnection

            conexao.setRequestProperty("Content-Type", "application/json")
            conexao.setRequestProperty("Accept", "application/json")
            conexao.requestMethod = "POST"

            conexao.doInput = true

            val output = PrintStream(conexao.outputStream)
            output.print(jsCelular)

            conexao.connect()

            val scanner = Scanner(conexao.inputStream)
            val resposta = scanner.nextLine()

            val codCel = JSONObject(resposta).getInt("codCelular")
            val cel = JSONObject(resposta).getString("celular")

            val retornoCelular = Celular(codCel, cel)

            uiThread {
                cadastrarCliente(retornoCelular)
            }

        }


    }

    fun cadastrarCliente(celular: Celular) {

        val txtNome = findViewById<TextView>(R.id.txt_nome_cliente)
        val txtSobrenome = findViewById<TextView>(R.id.txt_sobrenome_cliente)
        val txtCpf = findViewById<TextView>(R.id.txt_cpf_cliente)
        val txtDtNascimento = findViewById<TextView>(R.id.txt_dt_nascimento_cliente)
        val txtEmail = findViewById<TextView>(R.id.txt_email_cliente)
        val txtSenha = findViewById<TextView>(R.id.txt_senha_cliente)
        val spnSexo = findViewById<Spinner>(R.id.spn_sexo_cliente)
        val txtConfirmarSenha = findViewById<TextView>(R.id.txt_confirma_senha_cliente)

        Toast.makeText(this, "NOME: " + txtNome.text.toString(), Toast.LENGTH_LONG).show()


//        ArrayAdapter.createFromResource(
//                this,
//                R.array.array_sexo,
//                android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spnSexo.adapter = adapter
//        }
//
//        val sexoSelecionado = spnSexo.selectedItem
//
//        val sexo = Verificacao.verificarSexo(sexoSelecionado.toString())
        val senha = Verificacao.verificarSenha(txtSenha.text.toString(), txtConfirmarSenha.text.toString())

        if (senha){

            val cliente = Cliente(0,
                    txtNome.text.toString(),
                    txtSobrenome.text.toString(),
                    txtCpf.text.toString(),
                    txtDtNascimento.text.toString(),
                    txtEmail.text.toString(),
                    txtSenha.text.toString(),
                    celular,
                    'F',
                    "teste.jpg")

            val url = URL("http://10.107.144.10:8080/cliente")

            doAsync {

                val jsCliente = JSONStringer()

                jsCliente.`object`()
                jsCliente.key("nome").value(cliente.nome)
                jsCliente.key("sobrenome").value(cliente.sobrenome)
                jsCliente.key("cpf").value(cliente.cpf)
                jsCliente.key("dtNasc").value(cliente.dtNasc)
                jsCliente.key("email").value(cliente.email)
                jsCliente.key("senha").value(cliente.senha)
                jsCliente.key("celular")
                        .`object`()
                        .key("codCelular")
                        .value(celular.codCelular)
                        .endObject()
                jsCliente.key("sexo").value("F")
                jsCliente.key("foto").value("Teste")
                jsCliente.endObject()

                val conexao = url.openConnection() as HttpURLConnection

                conexao.setRequestProperty("Content-Type", "application/json")
                conexao.setRequestProperty("Accept", "application/json")
                conexao.requestMethod = "POST"

                conexao.doInput = true

                val output = PrintStream(conexao.outputStream)
                output.print(jsCliente)

                conexao.connect()

                val scanner = Scanner(conexao.inputStream)
                val resposta = scanner.nextLine()


                uiThread {
                    alert("Cadastrado com sucesso!")
                }

            }

        } else {
            Toast.makeText(this,"Senhas não coincidem", Toast.LENGTH_LONG).show()
        }




    }
}