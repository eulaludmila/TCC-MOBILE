package br.senai.sp.estacionamento.tasks

import android.app.ProgressDialog
import android.content.Context
import android.opengl.Visibility
import android.os.AsyncTask
import android.support.constraint.ConstraintLayout
import android.text.TextWatcher
import android.view.View
import android.widget.ProgressBar

import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

import tcc.sp.senai.br.showdebolos.model.Endereco


@Suppress("DEPRECATION")
class CarregarEnderecoTasks(val endereco: Endereco, context: Context, var carregando:ConstraintLayout) : AsyncTask<Endereco, Endereco, Endereco>() {
    private var dados = ""
    val context = context

    override fun doInBackground(vararg params: Endereco?): Endereco? {



        try {

            val url = URL("https://viacep.com.br/ws/" + endereco.cep + "/json")

            val conexao = url.openConnection() as HttpURLConnection

            val dadosStream = conexao.inputStream

            val leitorStream = InputStreamReader(dadosStream)

            val bufferedReader = BufferedReader(leitorStream)

            var registro : String? = ""


            while (registro != null) {
                registro = bufferedReader.readLine()
                dados += registro
            }

            bufferedReader.close()

            try {



                val jsEndereco = JSONObject(dados)

                endereco.bairro = (jsEndereco.getString("bairro"))
                endereco.endereco = (jsEndereco.getString("logradouro"))
                endereco.cidade.cidade = (jsEndereco.getString("localidade"))
                endereco.cidade.estado.uf = (jsEndereco.getString("uf"))


            } catch (e: JSONException) {
                e.printStackTrace()
            }


        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return endereco
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onProgressUpdate(vararg values: Endereco?) {
        super.onProgressUpdate(*values)
    }

    override fun onPostExecute(result: Endereco?) {
        super.onPostExecute(result)

        carregando!!.setVisibility(View.INVISIBLE)

    }
}
