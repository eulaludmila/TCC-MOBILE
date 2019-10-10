package tcc.sp.senai.br.showdebolos.services

import retrofit2.create

class ApiConfig {

    fun APIutils(){}

    companion object {
        val API_URL = "http://54.242.6.253:8080/foto/"

        fun getFotosService(): FotosService? {
            return RetrofitClient.getclient(API_URL)!!.create(FotosService::class.java)
        }

        fun getConfeiteiroService(): ConfeiteiroService? {
            return RetrofitClient.getConfeiteiro("http://54.242.6.253:8080/")!!.create(ConfeiteiroService::class.java)
        }
    }



}