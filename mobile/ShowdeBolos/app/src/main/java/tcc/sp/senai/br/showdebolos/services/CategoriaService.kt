package tcc.sp.senai.br.showdebolos.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import tcc.sp.senai.br.showdebolos.model.Categoria
import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import tcc.sp.senai.br.showdebolos.model.ConfeiteiroDTO

interface CategoriaService {

    @GET("categoria")
    fun buscarCategoria(): Call<List<Categoria>>
}