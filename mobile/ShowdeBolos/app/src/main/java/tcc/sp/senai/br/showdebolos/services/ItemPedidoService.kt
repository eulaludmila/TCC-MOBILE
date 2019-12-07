package tcc.sp.senai.br.showdebolos.services

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import tcc.sp.senai.br.showdebolos.model.ItemPedido

interface ItemPedidoService {

    @POST("itemPedido/list")
    fun fazerPedido(@Body produto: RequestBody, @Header("authorization") token:String): Call<ItemPedido>

    @GET("itemPedido/{codPedido}")
    fun buscarItensPedido(@Path("codPedido") codPedido: Int, @Header("Authorization") token:String ): Call<List<ItemPedido>>

}