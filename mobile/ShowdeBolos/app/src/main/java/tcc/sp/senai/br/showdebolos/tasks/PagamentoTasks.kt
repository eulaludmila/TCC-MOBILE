package tcc.sp.senai.br.showdebolos.tasks

import android.os.AsyncTask
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.Toast
import org.json.JSONObject
import org.json.JSONStringer
import tcc.sp.senai.br.showdebolos.model.*
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class PagamentoTasks(val enderecoCliente: EnderecoCliente,
                     val enderecoConfeiteiro: EnderecoConfeiteiro,


                     val total:Int,
                     val pedido:Pedido,
                     val cartao: Cartao): AsyncTask<String, String, String>() {


    override fun doInBackground(vararg p0: String?): String? {
        val url = URL("https://api.pagar.me/1/transactions")

        val jsPagamento = JSONStringer()

        jsPagamento.`object`()
        jsPagamento.key("api_key").value("ak_test_hBRCkBIx3Y6xNGKTYyFkTA3l8GTv0d")
        jsPagamento.key("capture").value("false")
        jsPagamento.key("amount").value(total * 100)
        jsPagamento.key("card_number").value(cartao.numeroCartao)
        jsPagamento.key("card_cvv").value(cartao.cvv)
        jsPagamento.key("card_expiration_date").value(cartao.validade)
        jsPagamento.key("card_holder_name").value(cartao.nomeTitular)
        jsPagamento.key("customer")
            .`object`()
                .key("external_id")
                .value(enderecoCliente.cliente!!.codCliente)
                .key("name")
                .value("${enderecoCliente.cliente.nome} ${enderecoCliente.cliente.sobrenome}")
                .key("type")
                .value("individual")
                .key("country")
                .value("br")
                .key("email")
                .value(enderecoCliente.cliente.email)
                .key("documents")
                .array()
                    .`object`()
                        .key("type")
                        .value("cpf")
                        .key("number")
                        .value(enderecoCliente.cliente.cpf)
                    .endObject()
                .endArray()
                .key("phone_numbers")
                    .array()
                        .value(enderecoCliente.cliente.codCelular.celular)
                    .endArray()
                .key("birthday")
                .value(enderecoCliente.cliente.dtNasc)
            .endObject()
        jsPagamento.key("billing")
                .`object`()
                    .key("name")
                    .value("${enderecoCliente.cliente.nome} ${enderecoCliente.cliente.sobrenome}")
                    .key("address")
                        .`object`()
                            .key("country")
                            .value("br")
                            .key("state")
                            .value(enderecoCliente.endereco.cidade.estado.uf)
                            .key("city")
                            .value(enderecoCliente.endereco.cidade.cidade)
                            .key("neighborhood")
                            .value(enderecoCliente.endereco.bairro)
                            .key("street")
                            .value(enderecoCliente.endereco.endereco)
                            .key("street_number")
                            .value(enderecoCliente.endereco.numero)
                            .key("zipcode")
                            .value(enderecoCliente.endereco.cep)
                        .endObject()
                .endObject()
        jsPagamento.key("shipping")
                .`object`()
                    .key("name")
                    .value(enderecoConfeiteiro.confeiteiro.nome)
                    .key("fee")
                    .value(0)
                    .key("delivery_date")
                    .value(pedido.dataEntrega)
                    .key("expedited")
                    .value("false")
                    .key("address")
                        .`object`()
                            .key("country")
                            .value("br")
                            .key("state")
                            .value(enderecoConfeiteiro.endereco.cidade.estado.uf)
                            .key("city")
                            .value(enderecoConfeiteiro.endereco.cidade.cidade)
                            .key("neighborhood")
                            .value(enderecoConfeiteiro.endereco.bairro)
                            .key("street")
                            .value(enderecoConfeiteiro.endereco.endereco)
                            .key("street_number")
                            .value(enderecoConfeiteiro.endereco.numero)
                            .key("zipcode")
                            .value(enderecoConfeiteiro.endereco.cep)
                        .endObject()
                .endObject()
        jsPagamento.key("items")
                .array()
                    .`object`()
                        .key("id")
                        .value("1")
                        .key("title")
                        .value("Teste")
                        .key("unit_price")
                        .value("1000")
                        .key("quantity")
                        .value("1")
                        .key("tangible")
                        .value("true")
                    .endObject()
                .endArray()
        jsPagamento.endObject()

        val conexao = url.openConnection() as HttpURLConnection

        conexao.setRequestProperty("Content-Type", "application/json")
        conexao.setRequestProperty("Accept", "application/json")
        conexao.requestMethod = "POST"

        conexao.doInput = true

        val output = PrintStream(conexao.outputStream)
        output.print(jsPagamento)

        conexao.connect()

        val scanner = Scanner(conexao.inputStream)
        val resposta = scanner.nextLine()

        val retorno = JSONObject(resposta)

        val status = retorno.getString("status")


        return status
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

//        carregando!!.visibility = View.INVISIBLE

    }


}