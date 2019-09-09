package br.senai.sp.estacionamento.tasks

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.text.TextWatcher

import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

import tcc.sp.senai.br.showdebolos.model.Endereco


class CarregarEndereco(val endereco: Endereco, context: Context) : AsyncTask<Endereco, Endereco, Endereco>() {
    private var dados = ""
    private var progressDialog: ProgressDialog? = null
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
                endereco.logradouro = (jsEndereco.getString("logradouro"))
                endereco.cidade = (jsEndereco.getString("localidade"))
                endereco.uf = (jsEndereco.getString("uf"))


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
        progressDialog = ProgressDialog(context)
        progressDialog!!.setTitle("Carregando dados")
        progressDialog!!.setMessage("Aguarde...")
        progressDialog!!.show()
    }

    override fun onPostExecute(result: Endereco?) {
        super.onPostExecute(result)

        progressDialog!!.dismiss()
    }
}