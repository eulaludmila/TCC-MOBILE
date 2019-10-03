package tcc.sp.senai.br.showdebolos.tasks

import android.os.AsyncTask
import android.widget.Toast
import org.json.JSONObject
import org.json.JSONStringer
import tcc.sp.senai.br.showdebolos.model.*
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class CadastrarEnderecoTasks(val endereco: Endereco): AsyncTask<Endereco,Endereco, Endereco>() {


    override fun doInBackground(vararg params: Endereco?): Endereco? {
        val url = URL("http://54.242.6.253:8080/endereco")

        val jsCliente = JSONStringer()

        jsCliente.`object`()
                .key("endereco").value(endereco.endereco)
                .key("numero").value(endereco.numero)
                .key("complemento").value(endereco.complemento)
                .key("cep").value(endereco.cep)
                .key("bairro").value(endereco.bairro)
                .key("cidade")
                    .`object`()
                    .key("cidade")
                    .value(endereco.codCidade.cidade)
                    .key("estado")
                        .`object`()
                        .key("uf")
                        .value(endereco.codCidade.codEstado.uf)
                        .endObject()
                    .endObject()
                .endObject()

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

        val joEndereco = JSONObject(resposta)

        val joCidade = joEndereco.getJSONObject("cidade")


        val joEstado = joCidade.getJSONObject("estado")

        val estado = Estado(joEstado.getInt("codEstado"),
                            joEstado.getString("uf"),
                            joEstado.getString("estado"))

        val cidade = Cidade(joCidade.getInt("codCidade"),
                joCidade.getString("cidade"),
                estado)

        val retornoEndereco= Endereco(joEndereco.getInt("codEndereco"),
                joEndereco.getString("endereco"),
                joEndereco.getString("numero"),
                joEndereco.getString("complemento"),
                joEndereco.getString("cep"),
                joEndereco.getString("bairro"),
                cidade)



        return retornoEndereco
    }


}