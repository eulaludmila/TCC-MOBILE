package tcc.sp.senai.br.showdebolos.tasks

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import org.json.JSONStringer
import tcc.sp.senai.br.showdebolos.model.*
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class CadastrarEnderecoClienteTasks(val enderecoCliente: EnderecoCliente, val token:String) : AsyncTask<Cliente, Cliente, Cliente>() {


    override fun doInBackground(vararg params: Cliente?): Cliente? {
        val url = URL("http://3.232.178.219:8080/enderecocliente")

        val jsEnderecoCliente = JSONStringer()

        jsEnderecoCliente.`object`()
        jsEnderecoCliente.key("cliente")
                .`object`()
                .key("codCliente").value(enderecoCliente.cliente!!.codCliente)

                .endObject()
        jsEnderecoCliente.key("endereco")
                .`object`()
                .key("codEndereco").value(enderecoCliente.endereco.codEndereco)
                .endObject()

                .endObject()

        Log.d("JSON_ENDERECO", jsEnderecoCliente.toString())


        val conexao = url.openConnection() as HttpURLConnection

        conexao.setRequestProperty("Content-Type", "application/json")
        conexao.setRequestProperty("Accept", "application/json")
        conexao.setRequestProperty("Authorization", token)
        conexao.requestMethod = "POST"

        conexao.doInput = true

        Log.d("ENDERECOCLIENTE", jsEnderecoCliente.toString())

        val output = PrintStream(conexao.outputStream)
        output.print(jsEnderecoCliente)

        conexao.connect()

        val scanner = Scanner(conexao.inputStream)
        val resposta = scanner.nextLine()

        val joEnderecoConfeiteiro = JSONObject(resposta)

        val joEndereco = joEnderecoConfeiteiro.getJSONObject("endereco")



//        val joCelular = joConfeiteiro.getJSONObject("celular")
//
//        val celular = Celular(joCelular.getInt("codCelular"),
//                joCelular.getString("celular"))
//
//
//
//        val retornoCliente = Cliente(joConfeiteiro.getInt("codCliente"),
//                joConfeiteiro.getString("nome"),
//                joConfeiteiro.getString("sobrenome"),
//                joConfeiteiro.getString("cpf"),
//                joConfeiteiro.getString("dtNasc"),
//                joConfeiteiro.getString("email"),
//                joConfeiteiro.getString("senha"),
//                celular,
//                joConfeiteiro.getString("sexo"),
//                joConfeiteiro.getString("foto"))
//
//
//
//        return retornoCliente

        return enderecoCliente.cliente
    }


}