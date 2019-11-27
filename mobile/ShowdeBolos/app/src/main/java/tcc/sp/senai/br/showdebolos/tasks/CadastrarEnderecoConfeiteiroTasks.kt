package tcc.sp.senai.br.showdebolos.tasks

import android.os.AsyncTask
import org.json.JSONObject
import org.json.JSONStringer
import tcc.sp.senai.br.showdebolos.model.*
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class CadastrarEnderecoConfeiteiroTasks(val enderecoConfeiteiro: EnderecoConfeiteiro) : AsyncTask<Confeiteiro, Confeiteiro, Confeiteiro>() {


    override fun doInBackground(vararg params: Confeiteiro?): Confeiteiro? {
        val url = URL("http://3.232.178.219:8080/enderecoconfeiteiro/mobile")

        val jsEnderecoConfeiteiro = JSONStringer()

        jsEnderecoConfeiteiro.`object`()
        jsEnderecoConfeiteiro.key("confeiteiro")
                .`object`()
                .key("codConfeiteiro").value(enderecoConfeiteiro.confeiteiro.codConfeiteiro)

                .endObject()
        jsEnderecoConfeiteiro.key("endereco")
                .`object`()
                .key("codEndereco").value(enderecoConfeiteiro.endereco.codEndereco)
                .endObject()

                .endObject()


        val conexao = url.openConnection() as HttpURLConnection

        conexao.setRequestProperty("Content-Type", "application/json")
        conexao.setRequestProperty("Accept", "application/json")
        conexao.requestMethod = "POST"

        conexao.doInput = true

        val output = PrintStream(conexao.outputStream)
        output.print(jsEnderecoConfeiteiro)

        conexao.connect()

        val scanner = Scanner(conexao.inputStream)
        val resposta = scanner.nextLine()

        val joEnderecoConfeiteiro = JSONObject(resposta)

        val joConfeiteiro = joEnderecoConfeiteiro.getJSONObject("confeiteiro")



        val joCelular = joConfeiteiro.getJSONObject("celular")

        val celular = Celular(joCelular.getInt("codCelular"),
                joCelular.getString("celular"))



        val retornoConfeiteiro = Confeiteiro(joConfeiteiro.getInt("codConfeiteiro"),
                joConfeiteiro.getString("nome"),
                joConfeiteiro.getString("sobrenome"),
                joConfeiteiro.getString("cpf"),
                joConfeiteiro.getString("dtNasc"),
                joConfeiteiro.getString("email"),
                joConfeiteiro.getString("senha"),
                celular,
                joConfeiteiro.getString("sexo"),
                joConfeiteiro.getString("foto"),
                joConfeiteiro.getDouble("avaliacao"))



        return retornoConfeiteiro
    }


}