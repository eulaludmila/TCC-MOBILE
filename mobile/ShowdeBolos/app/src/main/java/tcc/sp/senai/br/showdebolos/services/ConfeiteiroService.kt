package tcc.sp.senai.br.showdebolos.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import tcc.sp.senai.br.showdebolos.model.ConfeiteiroDTO
import tcc.sp.senai.br.showdebolos.model.EnderecoConfeiteiro

interface ConfeiteiroService {

    @GET("enderecoconfeiteiro")
    fun buscarConfeiteiros(@Header("Authorization") token:String ): Call<List<EnderecoConfeiteiro>>

    @GET("enderecoconfeiteiro")
    fun buscarTodosConfeiteiros(@Header("Authorization") token:String ): Call<List<EnderecoConfeiteiro>>

    @GET("enderecoconfeiteiro/{codConfeiteiro}")
    fun buscarConfeiteiro(@Header("Authorization") token:String, @Path("codConfeiteiro") codConfeiteiro: String): Call<EnderecoConfeiteiro>
}