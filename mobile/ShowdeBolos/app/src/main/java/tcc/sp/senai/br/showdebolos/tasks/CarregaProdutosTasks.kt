package tcc.sp.senai.br.showdebolos.tasks

import android.os.AsyncTask
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.http.QueryName
import tcc.sp.senai.br.showdebolos.model.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class CarregaProdutosTasks(val produtos: ArrayList<Produto>): AsyncTask<ArrayList<Produto>, ArrayList<Produto>, ArrayList<Produto>>() {

    private var dados = ""

    override fun doInBackground(vararg params: ArrayList<Produto>?): ArrayList<Produto>? {
//
//        try{
//
//            val url = URL("http://54.242.6.253:8080/produto")
//
//            val conexao = url.openConnection() as HttpURLConnection
//
//            val dadosStream = conexao.inputStream
//
//            val leitorStream = InputStreamReader(dadosStream)
//
//            val bufferedReader = BufferedReader(leitorStream)
//
//            var registro : String? = ""
//
//
//            while (registro != null) {
//                registro = bufferedReader.readLine()
//                dados += registro
//            }
//
//            bufferedReader.close()
//
//            try{
//
////                val jsProduto = JSONArray(dados)
////
////                val joConfeiteiro = jsProduto.getJSONArray("confeiteiro")
////                val joCelular = joConfeiteiro.getJSONArray("celular")
////                val celular = Celular(joCelular.getInt("codCelular"),
////                        joCelular.getString("celular"))
////                val confeiteiro = Confeiteiro(joConfeiteiro.getInt("codConfeiteiro"),
////                        joConfeiteiro.getString("nome"),
////                        joConfeiteiro.getString("sobrenome"),
////                        joConfeiteiro.getString("cpf"),
////                        joConfeiteiro.getString("dtNasc"),
////                        joConfeiteiro.getString("email"),
////                        joConfeiteiro.getString("senha"),
////                        celular,
////                        joConfeiteiro.getString("foto"),
////                        joConfeiteiro.getString("sexo"))
////
////                val joCategoria = jsProduto.getJSONArray("categoria")
////
////                val categoria = Categoria(joCategoria.getInt("codCategoria"),
////                                            joCategoria.getString("categoria"),
////                                            joCategoria.getString("tipoUni"))
////
////                val joQuantidade = jsProduto.getJSONObject("quantidade")
////
////                val quantidade = Quantidade(joQuantidade.getInt("codCategoria"),
////                        joQuantidade.getInt("multiplo"),
////                        joQuantidade.getInt("maximo"))
////
////
////
////
////                for (i in 1..5){
////
////                    val produto = Produto(0, (jsProduto.getString("nomeProduto")),(jsProduto.getString("foto")),(jsProduto.getInt("preco").toDouble()),(jsProduto.getBoolean("status")))
//
//                    produtos.add(produto)
//                }
//
//            }catch (e:JSONException){
//                e.printStackTrace()
//            }
//
//        }catch (e: MalformedURLException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
       return produtos

    }
}