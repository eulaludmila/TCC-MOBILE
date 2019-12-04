package tcc.sp.senai.br.showdebolos.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import tcc.sp.senai.br.showdebolos.model.Pedido

interface PedidoService {

    @GET("pedido/cliente/pagamento/mobile/{codCliente}")
    fun buscarPedidosParaSeremPagos(@Path("codCliente") codCliente: String, @Header("Authorization") token:String ): Call<List<Pedido>>

    @GET("pedido/aguarde/mobile/{codConfeiteiro}")
    fun buscarItemPedidoEmAguarde(@Path("codConfeiteiro") codConfeiteiro: String, @Header("Authorization") token:String ): Call<List<Pedido>>

    @GET("pedido/cliente/{codCliente}")
    fun buscarPedidoCliente(@Path("codCliente") codCliente: String, @Header("Authorization") token:String ): Call<List<Pedido>>

}