package tcc.sp.senai.br.showdebolos

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.senai.sp.estacionamento.tasks.CarregarEnderecoTasks
import kotlinx.android.synthetic.main.activity_cadastro_endereco_cliente.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.model.*
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import tcc.sp.senai.br.showdebolos.tasks.CadastrarEnderecoClienteTasks
import tcc.sp.senai.br.showdebolos.tasks.CadastrarEnderecoTasks
import tcc.sp.senai.br.showdebolos.utils.JWTUtils
import java.io.IOException
import java.util.concurrent.ExecutionException

class CadastroEnderecoClienteActivity : AppCompatActivity() {

    var mPreferences: SharedPreferences? = null
    var mEditor: SharedPreferences.Editor? = null
    var perfil:Cliente? = null
    var token:String? = null
    var parts:List<String>? = null
    var idPerfil:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_endereco_cliente)

        mPreferences = this!!.getSharedPreferences("idValue", 0)
        token = mPreferences!!.getString("token","")

        idPerfil  = mPreferences!!.getString("codUsuario","")


        val estado = Estado(0,txt_uf_cliente.text.toString(), "")

        val cidade = Cidade(0,txt_cidade_cliente.text.toString(), estado)

        val endereco = Endereco(0,
                txt_rua_cliente.text.toString(),
                txt_numero_cliente.text.toString(),
                txt_complemento_cliente.text.toString(),
                txt_cep_cliente.text.toString(),
                txt_bairro_cliente.text.toString(),
                cidade)

        txt_cep_cliente.addTextChangedListener(Mask.mask("#####-###", txt_cep_cliente))


        val callPerfil = ApiConfig.getClienteService().buscarCliente(this!!.token!!, this!!.idPerfil!!)

        callPerfil.enqueue(object : retrofit2.Callback<Cliente>{
            override fun onFailure(call: Call<Cliente>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<Cliente>?, response: Response<Cliente>?) {
                perfil = response!!.body()

                Log.d("CLIENTE", perfil.toString())
            }
        })



        txt_cep_cliente.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                //Criação da variavel com o valor do cep digitado
                val cep = txt_cep_cliente.text.toString()

                //Inseção do cep na classe endereço
                endereco.cep = cep

                //Verificação se foram feito todos os digitos do cep
                if (txt_cep_cliente.length() == 9){

                    val carregarEndereco = CarregarEnderecoTasks(endereco, this@CadastroEnderecoClienteActivity, carregando_cliente)
                    carregando_cliente!!.setVisibility(View.VISIBLE)
                    Handler().postDelayed({

                        //Executando a função que traz o JSON do viacep
                        carregarEndereco.execute()


                        try {

                            //Colocando os dados do JSON em suas respectivas caixas de texto
                            val endereco = carregarEndereco.get() as Endereco
                            txt_cidade_cliente.setText(endereco.cidade.cidade)
                            txt_rua_cliente.setText(endereco.endereco)
                            txt_bairro_cliente.setText(endereco.bairro)
                            txt_uf_cliente.setText(endereco.cidade.estado.uf)


                        }catch (e : ExecutionException){
                            e.printStackTrace()
                        }catch (e: InterruptedException){
                            e.printStackTrace()
                        }
                    }, 100)
                }


            }



            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                txt_cidade_cliente.setText("")
                txt_rua_cliente.setText("")
                txt_bairro_cliente.setText("")
                txt_uf_cliente.setText("")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                txt_cidade_cliente.setText("")
                txt_rua_cliente.setText("")
                txt_bairro_cliente.setText("")
                txt_uf_cliente.setText("")


            }

        })

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val menuInflater = menuInflater

        menuInflater.inflate(R.menu.context_menu, menu)


        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.confirmar -> {

                val estado = Estado(0,txt_uf_cliente.text.toString(), "")

                val cidade = Cidade(0,txt_cidade_cliente.text.toString(), estado)

                val endereco = Endereco(0, txt_rua_cliente.text.toString(),txt_numero_cliente.text.toString(),txt_complemento_cliente.text.toString(),txt_cep_cliente.text.toString(),txt_bairro_cliente.text.toString(), cidade)




                val cadastroEndereco = CadastrarEnderecoTasks(endereco)
                cadastroEndereco.execute()
                val retornoEndereco = cadastroEndereco.get() as Endereco


                val enderecoCliente = EnderecoCliente(0, perfil, retornoEndereco)


                val cadastroEnderecoCliente = CadastrarEnderecoClienteTasks(enderecoCliente, this!!.token!!)
                cadastroEnderecoCliente.execute()

                finish()

            }


            else -> {
            }
        }

        return super.onOptionsItemSelected(item)
    }

    @Throws(IOException::class)
    fun modifyOrientation(bitmap: Bitmap, image_absolute_path: String): Bitmap {
        val ei = ExifInterface(image_absolute_path)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> return rotate(bitmap, 90f)

            ExifInterface.ORIENTATION_ROTATE_180 -> return rotate(bitmap, 180f)

            ExifInterface.ORIENTATION_ROTATE_270 -> return rotate(bitmap, 270f)

            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> return flip(bitmap, true, false)

            ExifInterface.ORIENTATION_FLIP_VERTICAL -> return flip(bitmap, false, true)

            else -> return bitmap
        }
    }



    fun rotate(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap {
        val matrix = Matrix()
        matrix.preScale((if (horizontal) -1 else 1).toFloat(), (if (vertical) -1 else 1).toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}
