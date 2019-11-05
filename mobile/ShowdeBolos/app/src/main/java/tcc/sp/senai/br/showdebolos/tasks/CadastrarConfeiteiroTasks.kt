package tcc.sp.senai.br.showdebolos.tasks

import android.os.AsyncTask
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.Toast
import org.json.JSONObject
import org.json.JSONStringer
import tcc.sp.senai.br.showdebolos.model.Celular
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import tcc.sp.senai.br.showdebolos.model.Endereco
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class CadastrarConfeiteiroTasks(val confeiteiro: Confeiteiro, var carregando: ConstraintLayout): AsyncTask<Confeiteiro, Confeiteiro, Confeiteiro>() {


    override fun doInBackground(vararg params: Confeiteiro?): Confeiteiro? {
        val url = URL("http://3.232.178.219:8080/confeiteiro")

        val jsCliente = JSONStringer()

        jsCliente.`object`()
        jsCliente.key("nome").value(confeiteiro.nome)
        jsCliente.key("sobrenome").value(confeiteiro.sobrenome)
        jsCliente.key("cpf").value(confeiteiro.cpf)
        jsCliente.key("dtNasc").value(confeiteiro.dtNasc)
        jsCliente.key("email").value(confeiteiro.email)
        jsCliente.key("senha").value(confeiteiro.senha)
        jsCliente.key("celular")
                .`object`()
                .key("celular")
                .value(confeiteiro.celular.celular)
                .endObject()
        jsCliente.key("sexo").value(confeiteiro.sexo)
        jsCliente.key("foto").value(confeiteiro.foto)
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

        val joConfeiteiro = JSONObject(resposta)

        val joCelular = joConfeiteiro.getJSONObject("celular")

        val celular = Celular(joCelular.getInt("codCelular"),
                joCelular.getString("celular"))

        val retornoConfeiteiro = Confeiteiro(JSONObject(resposta).getInt("codConfeiteiro"),
                JSONObject(resposta).getString("nome"),
                JSONObject(resposta).getString("sobrenome"),
                JSONObject(resposta).getString("cpf"),
                JSONObject(resposta).getString("dtNasc"),
                JSONObject(resposta).getString("email"),
                JSONObject(resposta).getString("senha"),
                celular,
                JSONObject(resposta).getString("foto"),
                JSONObject(resposta).getString("sexo"),
                JSONObject(resposta).getDouble("avaliacao"))



        return retornoConfeiteiro
    }

    override fun onPostExecute(result: Confeiteiro?) {
        super.onPostExecute(result)

        carregando!!.setVisibility(View.INVISIBLE)

    }


}