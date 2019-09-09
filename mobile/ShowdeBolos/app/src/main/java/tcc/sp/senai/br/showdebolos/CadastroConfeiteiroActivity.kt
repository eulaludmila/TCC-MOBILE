package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cadastro_confeiteiro.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import org.json.JSONStringer
import tcc.sp.senai.br.showdebolos.model.Celular
import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import tcc.sp.senai.br.util.Verificacao
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class CadastroConfeiteiroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_confeiteiro)

        txt_cpf_confeiteiro.addTextChangedListener(Mask.mask("###.###.###-##", txt_cpf_confeiteiro))
        txt_celular_confeiteiro.addTextChangedListener(Mask.mask("(##) #####-####", txt_celular_confeiteiro))
        txt_dt_nascimento_confeiteiro.addTextChangedListener(Mask.mask("##/##/####", txt_dt_nascimento_confeiteiro))

        txt_celular_confeiteiro.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                txt_celular_confeiteiro.hint = "Ex: (11) 98648-4646"
            }else{
                txt_celular_confeiteiro.hint = ""
            }
        }

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

        val txtCelular = findViewById<TextView>(R.id.txt_celular_confeiteiro)

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
                cadastrarConfeiteiro(retornoCelular)
            }

        }


    }

    fun cadastrarConfeiteiro(celular: Celular) {

        val txtNome = findViewById<TextView>(R.id.txt_nome_confeiteiro)
        val txtSobrenome = findViewById<TextView>(R.id.txt_sobrenome_confeiteiro)
        val txtCpf = findViewById<TextView>(R.id.txt_cpf_confeiteiro)
        val txtDtNascimento = findViewById<TextView>(R.id.txt_dt_nascimento_confeiteiro)
        val txtEmail = findViewById<TextView>(R.id.txt_email_confeiteiro)
        val txtSenha = findViewById<TextView>(R.id.txt_senha_confeiteiro)
        val spnSexo = findViewById<Spinner>(R.id.spn_sexo_confeiteiro)
        val txtConfirmarSenha = findViewById<TextView>(R.id.txt_confirma_senha_confeiteiro)

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

            val confeiteiro = Confeiteiro(0,
                    txtNome.text.toString(),
                    txtSobrenome.text.toString(),
                    txtCpf.text.toString(),
                    txtDtNascimento.text.toString(),
                    txtEmail.text.toString(),
                    txtSenha.text.toString(),
                    celular,
                    'F',
                    "teste.jpg")

            val url = URL("http://10.107.144.10:8080/confeiteiro")

            doAsync {

                val jsConfeiteiro = JSONStringer()

                jsConfeiteiro.`object`()
                jsConfeiteiro.key("nome").value(confeiteiro.nome)
                jsConfeiteiro.key("sobrenome").value(confeiteiro.sobrenome)
                jsConfeiteiro.key("cpf").value(confeiteiro.cpf)
                jsConfeiteiro.key("dtNasc").value(confeiteiro.dtNasc)
                jsConfeiteiro.key("email").value(confeiteiro.email)
                jsConfeiteiro.key("senha").value(confeiteiro.senha)
                jsConfeiteiro.key("celular")
                        .`object`()
                        .key("codCelular")
                        .value(celular.codCelular)
                        .endObject()
                jsConfeiteiro.key("sexo").value("F")
                jsConfeiteiro.key("foto").value("Teste")
                jsConfeiteiro.endObject()

                val conexao = url.openConnection() as HttpURLConnection

                conexao.setRequestProperty("Content-Type", "application/json")
                conexao.setRequestProperty("Accept", "application/json")
                conexao.requestMethod = "POST"

                conexao.doInput = true

                val output = PrintStream(conexao.outputStream)
                output.print(jsConfeiteiro)

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
