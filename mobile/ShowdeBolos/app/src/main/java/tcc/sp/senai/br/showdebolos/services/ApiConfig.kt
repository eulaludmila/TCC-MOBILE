package tcc.sp.senai.br.showdebolos.services

import retrofit2.create

class ApiConfig {

    fun APIutils(){}

    companion object {
        val API_URL = "http://10.107.144.21:8080/foto/"

        fun getFotosService(): FotosService? {
            return RetrofitClient.getclient(API_URL)!!.create(FotosService::class.java)
        }
    }



}