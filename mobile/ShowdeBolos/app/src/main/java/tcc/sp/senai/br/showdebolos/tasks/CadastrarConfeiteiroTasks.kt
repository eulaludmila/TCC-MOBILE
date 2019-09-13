package tcc.sp.senai.br.showdebolos.tasks

import android.os.AsyncTask
import org.json.JSONObject
import org.json.JSONStringer
import tcc.sp.senai.br.showdebolos.model.Celular
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class CadastrarConfeiteiroTasks(val confeiteiro: Confeiteiro) : AsyncTask<Confeiteiro, Confeiteiro, Confeiteiro>() {


    override fun doInBackground(vararg params: Confeiteiro?): Confeiteiro? {
        val url = URL("http://10.107.144.21:8080/enderecoconfeiteiro")

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
                .key("celular")
                .value(confeiteiro.codCelular.celular)
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
                JSONObject(resposta).getString("sexo"),
                JSONObject(resposta).getString("foto"))



        return retornoConfeiteiro
    }


}