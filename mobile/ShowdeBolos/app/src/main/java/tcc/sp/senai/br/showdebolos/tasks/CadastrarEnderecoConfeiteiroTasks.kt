package tcc.sp.senai.br.showdebolos.tasks

import android.os.AsyncTask
import org.json.JSONObject
import org.json.JSONStringer
import tcc.sp.senai.br.showdebolos.model.Cidade
import tcc.sp.senai.br.showdebolos.model.Endereco
import tcc.sp.senai.br.showdebolos.model.EnderecoConfeiteiro
import tcc.sp.senai.br.showdebolos.model.Estado
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class CadastrarEnderecoConfeiteiroTasks(val enderecoConfeiteiro: EnderecoConfeiteiro) : AsyncTask<Endereco, Endereco, Endereco>() {


    override fun doInBackground(vararg params:Endereco?):Endereco? {
        val url = URL("http://10.107.144.21:8080/enderecoconfeiteiro")

        val jsConfeiteiro = JSONStringer()

        jsConfeiteiro.`object`()
        jsConfeiteiro.key("").value(endereco.endereco)
        jsConfeiteiro.key("numero").value(endereco.numero)
        jsConfeiteiro.key("complemento").value(endereco.complemento)
        jsConfeiteiro.key("cep").value(endereco.cep)
        jsConfeiteiro.key("bairro").value(endereco.bairro)
        jsConfeiteiro.key("cidade").value(endereco.codCidade)
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

        val joEndereco = JSONObject(resposta)

        val joCidade = joEndereco.getJSONObject("cidade")

        val joEstado = joCidade.getJSONObject("estado")

        val estado = Estado(joEstado.getInt("codEstado"), joEstado.getString("estado"))


        val cidade = Cidade(joCidade.getInt("codCidade"), joCidade.getString("cidade"), estado)

        val retornoEndereco = Endereco(JSONObject(resposta).getInt("codEndereco"),
                JSONObject(resposta).getString("endereco"),
                JSONObject(resposta).getString("numero"),
                JSONObject(resposta).getString("complemento"),
                JSONObject(resposta).getString("cep"),
                JSONObject(resposta).getString("bairro"),
                cidade)



        return retornoEndereco
    }


}