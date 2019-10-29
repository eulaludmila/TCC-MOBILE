package tcc.sp.senai.br.showdebolos.tasks

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import org.json.JSONObject
import org.json.JSONStringer
import tcc.sp.senai.br.showdebolos.R
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import java.io.FileNotFoundException
import java.io.PrintStream
import java.lang.RuntimeException
import java.math.MathContext
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class LoginConfeiteiroTasks(val confeiteiro:Confeiteiro, val context: Context): AsyncTask<String, String, String>() {

    var token :String = ""

    override fun doInBackground(vararg p0: String?): String? {
        try {

            val url = URL("http://3.232.178.219:8080/login/confeiteiro")

            val jsCliente = JSONStringer()

            jsCliente.`object`()
            jsCliente.key("username").value(confeiteiro.email)
            jsCliente.key("password").value(confeiteiro.senha)
            jsCliente.endObject()

            val conexao = url.openConnection() as HttpURLConnection

            conexao.setRequestProperty("Content-Type", "application/json")
            conexao.setRequestProperty("Accept", "application/json")
            conexao.requestMethod = "POST"

            conexao.doInput = true

            val output = PrintStream(conexao.outputStream)
            output.print(jsCliente)

            conexao.connect()

            val scanner = Scanner(conexao.inputStream)
            val resposta = scanner.nextLine()

            val token = JSONObject(resposta).getString("token")

            this.token = token

        }catch (e:FileNotFoundException){
            e.printStackTrace()
            this.token = "erro"
        }catch (e:RuntimeException){
            e.printStackTrace()
            this.token = "erro"
        }catch (e:ConnectException){
            e.printStackTrace()
            this.token = "conexao"
        }

        return token

    }



}
