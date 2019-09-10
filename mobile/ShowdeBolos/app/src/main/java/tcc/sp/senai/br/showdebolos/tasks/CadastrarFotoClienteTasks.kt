package tcc.sp.senai.br.showdebolos.tasks

import android.os.AsyncTask
import tcc.sp.senai.br.showdebolos.model.Cliente
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL

class CadastrarFotoClienteTasks(val codCliente: Int, val imgArray: String): AsyncTask<String, String, String>() {
    override fun doInBackground(vararg params: String?): String? {
        val url = URL("10.107.144.21:8080/foto/mobile/cliente")



        val conexao = url.openConnection() as HttpURLConnection

        conexao.setRequestProperty("Content-Type", "application/json")
        conexao.setRequestProperty("Accept", "application/json")
        conexao.addRequestProperty("foto", imgArray)
        conexao.requestMethod = "POST"

        conexao.doInput = true

        conexao.connect()

        return "Deu certo"

    }


}