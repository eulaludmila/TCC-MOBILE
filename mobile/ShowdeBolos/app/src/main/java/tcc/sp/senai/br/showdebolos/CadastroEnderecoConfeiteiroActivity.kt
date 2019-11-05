package tcc.sp.senai.br.showdebolos

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import android.widget.Toast
import br.senai.sp.estacionamento.tasks.CarregarEnderecoTasks
import kotlinx.android.synthetic.main.activity_cadastro_endereco_confeiteiro.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.model.*
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import tcc.sp.senai.br.showdebolos.services.FotosService
import tcc.sp.senai.br.showdebolos.tasks.CadastrarConfeiteiroTasks
import tcc.sp.senai.br.showdebolos.tasks.CadastrarEnderecoConfeiteiroTasks
import tcc.sp.senai.br.showdebolos.tasks.CadastrarEnderecoTasks
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutionException

class CadastroEnderecoConfeiteiroActivity : AppCompatActivity() {

    private var fotoService:FotosService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_endereco_confeiteiro)

        fotoService = ApiConfig.getFotosService()

        val estado = Estado(0,txt_uf_confeiteiro.text.toString(), "")

        val cidade = Cidade(0,txt_cidade_confeiteiro.text.toString(), estado)

        val endereco = Endereco(0,
                txt_rua_cliente.text.toString(),
                txt_numero_confeiteiro.text.toString(),
                txt_complemento_cliente.text.toString(),
                txt_cep_confeiteiro.text.toString(),
                txt_bairro_confeiteiro.text.toString(),
                cidade)




        txt_cep_confeiteiro.addTextChangedListener(Mask.mask("#####-###", txt_cep_confeiteiro))


        txt_cep_confeiteiro.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                //Criação da variavel com o valor do cep digitado
                val cep = txt_cep_confeiteiro.text.toString()

                //Inseção do cep na classe endereço
                endereco.cep = cep

                //Verificação se foram feito todos os digitos do cep
                if (txt_cep_confeiteiro.length() == 9){

                    val carregarEndereco = CarregarEnderecoTasks(endereco, this@CadastroEnderecoConfeiteiroActivity, carregando)
                    carregando!!.setVisibility(View.VISIBLE)
                    Handler().postDelayed({

                        //Executando a função que traz o JSON do viacep
                        carregarEndereco.execute()


                    try {

                        //Colocando os dados do JSON em suas respectivas caixas de texto
                        val endereco = carregarEndereco.get() as Endereco
                        txt_cidade_confeiteiro.setText(endereco.cidade.cidade)
                        txt_rua_cliente.setText(endereco.endereco)
                        txt_bairro_confeiteiro.setText(endereco.bairro)
                        txt_uf_confeiteiro.setText(endereco.cidade.estado.uf)


                    }catch (e : ExecutionException){
                        e.printStackTrace()
                    }catch (e: InterruptedException){
                        e.printStackTrace()
                    }
                    }, 100)
                }


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                txt_cidade_confeiteiro.setText("")
                txt_rua_cliente.setText("")
                txt_bairro_confeiteiro.setText("")
                txt_uf_confeiteiro.setText("")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                txt_cidade_confeiteiro.setText("")
                txt_rua_cliente.setText("")
                txt_bairro_confeiteiro.setText("")
                txt_uf_confeiteiro.setText("")


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

                val estado = Estado(0,txt_uf_confeiteiro.text.toString(), "")

                val cidade = Cidade(0,txt_cidade_confeiteiro.text.toString(), estado)

                val endereco = Endereco(0, txt_rua_cliente.text.toString(),txt_numero_confeiteiro.text.toString(),txt_complemento_cliente.text.toString(),txt_cep_confeiteiro.text.toString(),txt_bairro_confeiteiro.text.toString(), cidade)

                val intent = intent
                val confeiteiro: Confeiteiro = intent.getSerializableExtra("confeiteiro") as Confeiteiro


                val cadastrarConfeiteiroTasks = CadastrarConfeiteiroTasks(confeiteiro, carregando)
                cadastrarConfeiteiroTasks.execute()

                val retornoConfeiteiro = cadastrarConfeiteiroTasks.get() as Confeiteiro

                val cadastroEndereco = CadastrarEnderecoTasks(endereco)
                cadastroEndereco.execute()

                val retornoEndereco = cadastroEndereco.get() as Endereco

                val enderecoConfeiteiro = EnderecoConfeiteiro(0, retornoConfeiteiro, retornoEndereco)


                val cadastroEnderecoConfeiteiro = CadastrarEnderecoConfeiteiroTasks(enderecoConfeiteiro)
                cadastroEnderecoConfeiteiro.execute()

                uploadImage(retornoConfeiteiro)

                finish()

            }


            else -> {
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun uploadImage(confeiteiro: Confeiteiro){

        val file = File(confeiteiro.foto)
        val oldBitmap: Bitmap = BitmapFactory.decodeFile(file.path)
        val bitmap = modifyOrientation(oldBitmap,file.path)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30,stream)
        val image  = stream.toByteArray()
        val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), image)
        val codBody = RequestBody.create(MediaType.parse("text/plain"), confeiteiro.codConfeiteiro.toString())
        val body = MultipartBody.Part.createFormData("foto", file.name, imageBody)

        val call = fotoService!!.uploadImageConfeiteiro(body, codBody)

        call?.enqueue(object : Callback<Confeiteiro> {

            override fun onResponse(call: Call<Confeiteiro>?, response: Response<Confeiteiro>?) {
                if(response!!.isSuccessful){
//                    Toast.makeText(this@CadastroConfeiteiroActivity, "Imagem Enviada com Sucesso", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Confeiteiro>?, t: Throwable?) {
                Toast.makeText(this@CadastroEnderecoConfeiteiroActivity, "ERRO!!! + ${t!!.message}", Toast.LENGTH_LONG).show()
                Log.d("ERRO IMAGEM", t.message)
            }



        })


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
