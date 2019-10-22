package tcc.sp.senai.br.showdebolos.services

import retrofit2.Call
import retrofit2.http.GET
import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import tcc.sp.senai.br.showdebolos.model.ConfeiteiroDTO
import tcc.sp.senai.br.showdebolos.model.EnderecoConfeiteiro

interface ConfeiteiroService {

    @GET("enderecoconfeiteiro")
    fun buscarConfeiteiros(): Call<List<EnderecoConfeiteiro>>

    @GET("confeiteiroDTO")
    fun buscarTodosConfeiteiros(): Call<List<ConfeiteiroDTO>>
}