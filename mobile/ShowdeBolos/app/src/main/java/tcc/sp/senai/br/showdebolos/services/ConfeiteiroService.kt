package tcc.sp.senai.br.showdebolos.services

import retrofit2.Call
import retrofit2.http.GET
import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import tcc.sp.senai.br.showdebolos.model.ConfeiteiroDTO

interface ConfeiteiroService {

    @GET("confeiteiroDTO/avaliacao")
    fun buscarConfeiteiros(): Call<List<ConfeiteiroDTO>>
}