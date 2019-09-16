package tcc.sp.senai.br.showdebolos.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class RetrofitClient {

    companion object {
        var retrofit: Retrofit? = null

        fun getclient(url: String): Retrofit? {
            if (retrofit == null) {

                retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit
        }
    }



}