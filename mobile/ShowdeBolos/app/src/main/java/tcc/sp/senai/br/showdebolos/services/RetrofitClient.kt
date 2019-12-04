package tcc.sp.senai.br.showdebolos.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.Gson





class RetrofitClient {

    companion object {
        var retrofit: Retrofit? = null

        fun getclient(url: String): Retrofit? {
            if (retrofit == null) {

                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }
            return retrofit
        }

        fun getCliente(url: String): Retrofit? {
            if (retrofit == null) {

                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }
            return retrofit
        }

        fun getConfeiteiro(url: String): Retrofit? {
            if (retrofit == null) {

                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }
            return retrofit
        }

        fun getCategoria(url: String): Retrofit? {
            if (retrofit == null) {

                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }
            return retrofit
        }

        fun getProduto(url: String): Retrofit? {
            if (retrofit == null) {

                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }
            return retrofit
        }

        fun getPedido(url: String): Retrofit? {
            if (retrofit == null) {

                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }
            return retrofit
        }

        fun getPedidoAguarde(url: String): Retrofit? {
            if (retrofit == null) {

                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }
            return retrofit
        }

        fun getPedidoCliente(url: String): Retrofit? {
            if (retrofit == null) {

                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }
            return retrofit
        }

    }



}