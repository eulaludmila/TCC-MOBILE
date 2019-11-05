package tcc.sp.senai.br.showdebolos.tasks

import android.os.AsyncTask
import android.widget.Toast
import org.json.JSONObject
import org.json.JSONStringer
import tcc.sp.senai.br.showdebolos.model.Celular
import tcc.sp.senai.br.showdebolos.model.Cliente
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class CadastrarClienteTasks(val cliente: Cliente, val celular: Celular): AsyncTask<Cliente, Cliente, Cliente>() {


    override fun doInBackground(vararg params: Cliente?): Cliente {
        val url = URL("http://3.232.178.219:8080/cliente")

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
                .key("celular")
                .value(celular.celular)
                .endObject()
        jsCliente.key("sexo").value(cliente.sexo)
        jsCliente.key("foto").value(cliente.foto)
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

        val joCliente = JSONObject(resposta)

        val joCelular = joCliente.getJSONObject("celular")

        val celular = Celular(joCelular.getInt("codCelular"),
                joCelular.getString("celular"))

        val retornoCliente = Cliente(JSONObject(resposta).getInt("codCliente"),
                                        JSONObject(resposta).getString("nome"),
                                        JSONObject(resposta).getString("sobrenome"),
                                        JSONObject(resposta).getString("cpf"),
                                        JSONObject(resposta).getString("dtNasc"),
                JSONObject(resposta).getString("email"),
                JSONObject(resposta).getString("senha"),
                celular,
                JSONObject(resposta).getString("sexo"),
                JSONObject(resposta).getString("foto"))



        return retornoCliente
    }


}