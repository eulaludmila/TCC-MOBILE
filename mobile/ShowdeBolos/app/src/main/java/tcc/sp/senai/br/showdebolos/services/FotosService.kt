package tcc.sp.senai.br.showdebolos.services

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.showdebolos.model.Foto


interface FotosService {

    @Multipart
    @POST("cliente")
    fun uploadImage(@Part image: MultipartBody): Call<Foto>
}