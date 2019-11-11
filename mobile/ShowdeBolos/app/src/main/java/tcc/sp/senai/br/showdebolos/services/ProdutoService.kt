package tcc.sp.senai.br.showdebolos.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import tcc.sp.senai.br.showdebolos.model.ConfeiteiroDTO
import tcc.sp.senai.br.showdebolos.model.Produto


interface ProdutoService {

    @GET("produto")
    fun buscarProduto(@Header("Authorization") token:String ): Call<List<Produto>>

    @GET("produto/categoria/{codCategoria}")
    fun buscarProdutoCategoria(@Path("codCategoria") codCategoriaService: String,@Header("Authorization") token:String ): Call<List<Produto>>

    @GET("produto")
    fun buscarProdutoId(): Call<List<Produto>>

    @GET("produto/confeiteiro/{codConfeiteiro}")
    fun buscarProdutoConfeiteiro(@Path("codConfeiteiro") codConfeiteiroDTO: String,@Header("Authorization") token:String ): Call<List<Produto>>

}