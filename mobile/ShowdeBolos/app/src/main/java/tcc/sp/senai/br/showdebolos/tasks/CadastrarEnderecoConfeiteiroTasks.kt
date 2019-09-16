package tcc.sp.senai.br.showdebolos.tasks

import android.os.AsyncTask
import org.json.JSONObject
import org.json.JSONStringer
import tcc.sp.senai.br.showdebolos.model.*
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class CadastrarEnderecoConfeiteiroTasks( val enderecoConfeiteiro: EnderecoConfeiteiro) : AsyncTask<Confeiteiro, Confeiteiro, Confeiteiro>() {


    override fun doInBackground(vararg params: Confeiteiro?): Confeiteiro? {
        val url = URL("http://10.107.144.21:8080/enderecoconfeiteiro")

        val jsEnderecoConfeiteiro = JSONStringer()

        jsEnderecoConfeiteiro.`object`()
        jsEnderecoConfeiteiro.key("confeiteiro")
                .`object`()
                .key("nome").value(enderecoConfeiteiro.codConfeiteiro.nome)
                .key("sobrenome").value(enderecoConfeiteiro.codConfeiteiro.sobrenome)
                .key("cpf").value(enderecoConfeiteiro.codConfeiteiro.cpf)
                .key("dtNasc").value(enderecoConfeiteiro.codConfeiteiro.dtNasc)
                .key("email").value(enderecoConfeiteiro.codConfeiteiro.email)
                .key("senha").value(enderecoConfeiteiro.codConfeiteiro.senha)
                .key("celular")
                .`object`()
                .key("celular")
                .value(enderecoConfeiteiro.codConfeiteiro.codCelular.celular)
                .endObject()
                .key("foto").value("Teste")
                .key("sexo").value("F")
                .endObject()
        jsEnderecoConfeiteiro.key("endereco")
                .`object`()
                .key("endereco").value(enderecoConfeiteiro.codEndereco.endereco)
                .key("numero").value(enderecoConfeiteiro.codEndereco.numero)
                .key("complemento").value(enderecoConfeiteiro.codEndereco.complemento)
                .key("cep").value(enderecoConfeiteiro.codEndereco.cep)
                .key("bairro").value(enderecoConfeiteiro.codEndereco.bairro)
                .key("cidade")
                .`object`()
                .key("cidade")
                .value(enderecoConfeiteiro.codEndereco.codCidade.cidade)
                .key("estado")
                .`object`()
                .key("uf")
                .value(enderecoConfeiteiro.codEndereco.codCidade.codEstado.uf)
                .endObject()
                .endObject()
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

        val joConfeiteiro = JSONObject(resposta)

        val joCelular = joConfeiteiro.getJSONObject("celular")

        val celular = Celular(joCelular.getInt("codCelular"),
                joCelular.getString("celular"))

//        val retornoConfeiteiro = Confeiteiro(JSONObject(resposta).getInt("codConfeiteiro"),
//                JSONObject(resposta).getString("nome"),
//                JSONObject(resposta).getString("sobrenome"),
//                JSONObject(resposta).getString("cpf"),
//                JSONObject(resposta).getString("dtNasc"),
//                JSONObject(resposta).getString("email"),
//                JSONObject(resposta).getString("senha"),
//                celular,
//                JSONObject(resposta).getString("sexo"),
//                JSONObject(resposta).getString("foto"))



        return null
    }


}