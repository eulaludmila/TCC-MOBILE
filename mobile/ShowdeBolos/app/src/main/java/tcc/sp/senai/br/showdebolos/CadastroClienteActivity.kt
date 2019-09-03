package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import java.net.HttpURLConnection
import java.net.URL
import java.io.PrintStream
import java.util.*


class CadastroClienteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cliente)

        txt_cpf_cliente.addTextChangedListener(Mask.mask("###.###.###-##", txt_cpf_cliente))
        txt_celular_cliente.addTextChangedListener(Mask.mask("(##) #####-####", txt_celular_cliente))
//        txt_dt_nascimento_cliente.addTextChangedListener(Mask.mask("##/##/####", txt_dt_nascimento_cliente))


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

        val celular = Celular(0, txt_celular_cliente.text.toString())

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

        val cliente = Cliente(0,
                txt_nome_cliente.text.toString(),
                txt_sobrenome_cliente.text.toString(),
                txt_cpf_cliente.text.toString(),
                txt_dt_nascimento_cliente.text.toString(),
                txt_email_cliente.text.toString(),
                txt_senha_cliente.text.toString(),
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
            jsCliente.key("codCelular").value(celular.codCelular)
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

            }

        }

    }
}