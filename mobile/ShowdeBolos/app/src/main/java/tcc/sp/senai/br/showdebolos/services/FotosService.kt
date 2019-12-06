package tcc.sp.senai.br.showdebolos.services

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import tcc.sp.senai.br.showdebolos.model.EnderecoConfeiteiro
import tcc.sp.senai.br.showdebolos.model.Foto


interface FotosService {

    @Multipart
    @POST("foto/cliente")
    fun uploadImageCliente(@Part image: MultipartBody.Part, @Part("codCliente") codCliente: RequestBody): Call<Cliente>

    @Multipart
    @POST("foto/confeiteiro")
    fun uploadImageConfeiteiro(@Part image: MultipartBody.Part, @Part("codConfeiteiro") codConfeiteiro: RequestBody): Call<Confeiteiro>

}