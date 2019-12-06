package tcc.sp.senai.br.showdebolos.services

import retrofit2.create
import tcc.sp.senai.br.showdebolos.model.Pedido

class ApiConfig {

    fun APIutils(){}

    companion object {

        fun getFotosService(): FotosService? {
            return RetrofitClient.getclient("http://3.232.178.219:8080/")!!.create(FotosService::class.java)
        }

        fun getConfeiteiroService(): ConfeiteiroService {
            return RetrofitClient.getConfeiteiro("http://3.232.178.219:8080/")!!.create(ConfeiteiroService::class.java)
        }

        fun getClienteService(): ClienteService {
            return RetrofitClient.getCliente("http://3.232.178.219:8080/")!!.create(ClienteService::class.java)
        }

        fun getEnderecoCliente(): EnderecoClienteService {
            return RetrofitClient.getCliente("http://3.232.178.219:8080/")!!.create(EnderecoClienteService::class.java)
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

        fun getItemPedidoEmAguarde():PedidoService{
            return RetrofitClient.getPedidoAguarde("http://3.232.178.219:8080/")!!.create(PedidoService::class.java)
        }

        fun getItemPedido():ItemPedidoService{
            return RetrofitClient.getPedidoAguarde("http://3.232.178.219:8080/")!!.create(ItemPedidoService::class.java)
        }

        fun getPedido():PedidoService{
            return RetrofitClient.getPedidoCliente("http://3.232.178.219:8080/")!!.create(PedidoService::class.java)
        }

    }



}