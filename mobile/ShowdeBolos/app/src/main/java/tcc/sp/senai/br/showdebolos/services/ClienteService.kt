package tcc.sp.senai.br.showdebolos.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import tcc.sp.senai.br.showdebolos.model.*

interface ClienteService {

    @GET("cliente/{codCliente}")
    fun buscarCliente(@Header("Authorization") token:String, @Path("codCliente") codCliente: String): Call<Cliente>

}