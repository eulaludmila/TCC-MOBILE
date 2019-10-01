package tcc.sp.senai.br.showdebolos.tasks

import android.os.AsyncTask
import kotlinx.android.synthetic.main.activity_cadastro_cliente.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL




class VerificarEmailCpfTasks(private val valor:String, val usuario:String, val tipo:String): AsyncTask<String, String, String>() {

    private var dados = ""

    override fun doInBackground(vararg params: String?): String? {
        val url = URL("http://54.242.6.253:8080/$usuario/$tipo/$valor")

        val conexao = url.openConnection() as HttpURLConnection

        val dadosStream = conexao.inputStream

        val leitorStream = InputStreamReader(dadosStream)

        val bufferedReader = BufferedReader(leitorStream)

        var registro : String? = ""

        registro = bufferedReader.readLine()

        bufferedReader.close()


        return registro


    }
}