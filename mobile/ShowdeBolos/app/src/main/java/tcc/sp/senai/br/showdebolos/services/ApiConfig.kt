package tcc.sp.senai.br.showdebolos.services

import retrofit2.create
import tcc.sp.senai.br.showdebolos.model.Pedido

class ApiConfig {

    fun APIutils(){}

    companion object {
        val API_URL = "http://3.232.178.219:8080/foto/"

        fun getFotosService(): FotosService? {
            return RetrofitClient.getclient(API_URL)!!.create(FotosService::class.java)
        }

        fun getConfeiteiroService(): ConfeiteiroService {
            return RetrofitClient.getConfeiteiro("http://3.232.178.219:8080/")!!.create(ConfeiteiroService::class.java)
        }

        fun getCategoriaService():CategoriaService{
            return RetrofitClient.getCategoria("http://3.232.178.219:8080/")!!.create(CategoriaService::class.java)
        }

        fun getProdutoService():ProdutoService{
            return RetrofitClient.getProduto("http://3.232.178.219:8080/")!!.create(ProdutoService::class.java)
        }

        fun getProdutoConfeiteiroService():ProdutoService{
            return RetrofitClient.getProduto("http://3.232.178.219:8080/")!!.create(ProdutoService::class.java)
        }

        fun getPedidosParaPagarService():PedidoService{
            return RetrofitClient.getPedido("http://3.232.178.219:8080/")!!.create(PedidoService::class.java)
        }

    }



}