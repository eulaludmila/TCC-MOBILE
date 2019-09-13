package tcc.sp.senai.br.showdebolos

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cadastro_cliente.*
import kotlinx.android.synthetic.main.activity_cadastro_confeiteiro.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.model.Celular

import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import tcc.sp.senai.br.showdebolos.model.Foto
import tcc.sp.senai.br.showdebolos.services.FotosService
import tcc.sp.senai.br.showdebolos.tasks.CadastrarConfeiteiroTasks
import tcc.sp.senai.br.utils.Verificacao
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class CadastroConfeiteiroActivity : AppCompatActivity() {

    val COD_IMAGE = 101
    //forçando a varialvel a ser nula
    var imageBitmap: Bitmap? = null
    var imagePath: String? = null
    var fotoService: FotosService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_confeiteiro)

        txt_cpf_confeiteiro.addTextChangedListener(Mask.mask("###.###.###-##", txt_cpf_confeiteiro))
        txt_celular_confeiteiro.addTextChangedListener(Mask.mask("(##) #####-####", txt_celular_confeiteiro))
        txt_dt_nascimento_confeiteiro.addTextChangedListener(Mask.mask("##/##/####", txt_dt_nascimento_confeiteiro))


        txt_celular_confeiteiro.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                txt_celular_confeiteiro.hint = "Ex: (11) 98648-4646"
            }else{
                txt_celular_confeiteiro.hint = ""
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val menuInflater = menuInflater

        menuInflater.inflate(R.menu.context_menu, menu)


        return super.onCreateOptionsMenu(menu)
    }

    //hora que o menu é selecionado
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.getItemId()) {
            R.id.confirmar -> {

                val txtNome = findViewById<TextView>(R.id.txt_nome_confeiteiro)
                val txtSobrenome = findViewById<TextView>(R.id.txt_sobrenome_confeiteiro)
                val txtCpf = findViewById<TextView>(R.id.txt_cpf_confeiteiro)
                val txtDtNascimento = findViewById<TextView>(R.id.txt_dt_nascimento_confeiteiro)
                val txtEmail = findViewById<TextView>(R.id.txt_email_confeiteiro)
                val txtSenha = findViewById<TextView>(R.id.txt_senha_confeiteiro)
                val spnSexo = findViewById<Spinner>(R.id.spn_sexo_confeiteiro)
                val txtConfirmarSenha = findViewById<TextView>(R.id.txt_confirma_senha_confeiteiro)
                val imgConfeiteiro = findViewById<ImageView>(R.id.img_confeiteiro)

                val txtCelular = findViewById<TextView>(R.id.txt_celular_confeiteiro)

                val celular = Celular(0, txtCelular.text.toString())

                val bitmap = (imgConfeiteiro.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val imgConfeiteiroByte = baos.toByteArray()
                val imgArray = Base64.getEncoder().encodeToString(imgConfeiteiroByte)




//                val sexo = Verificacao.verificarSexo(sexoSelecionado.toString())
                val senha = Verificacao.verificarSenha(txtSenha.text.toString(), txtConfirmarSenha.text.toString())

                val confeiteiro = Confeiteiro(0,
                        txtNome.text.toString(),
                        txtSobrenome.text.toString(),
                        txtCpf.text.toString(),
                        "04/06/2001",
                        txtEmail.text.toString(),
                        txtSenha.text.toString(),
                        celular,
                        "F",
                        "teste.png")

                val cadastrarConfeiteiro = CadastrarConfeiteiroTasks(confeiteiro)
                cadastrarConfeiteiro.execute()

                val retornoConfeiteiro = cadastrarConfeiteiro.get() as Confeiteiro

                val intent = Intent(this, CadastroEnderecoConfeiteiro::class.java)
                intent.putExtra("confeiteiro", retornoConfeiteiro.codConfeiteiro)
                startActivity(intent)



//                val cadastrarFoto = CadastrarFotoClienteTasks(retornoCliente.codCliente, imgArray)
//
//                cadastrarFoto.execute()
               // uploadImage(retornoConfeiteiro)

                //Toast.makeText(this, .toString(), Toast.LENGTH_LONG).show()
            }


            else -> {
            }
        }//abrir o banco
        //query de insert
        //fechar o banco
        return super.onOptionsItemSelected(item)
    }

    fun abrirGaleria(){
        //definindo a ação de conteudo
        val intent = Intent(Intent.ACTION_PICK)

        //definindo filtro para imagens
        intent.type = "image/*"

        //inicializando activity com resultado
        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), COD_IMAGE)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == COD_IMAGE && resultCode == Activity.RESULT_OK){
            if(data != null){
                //lendo a uri com a imagem
                val selectedImage: Uri = data.data
                val inputStream = contentResolver.openInputStream(data.data)


                //transformando o resultado em bitmap
                imageBitmap = BitmapFactory.decodeStream(inputStream)

                //exibir a imagem no aplicativo
                img_cliente.setImageBitmap(imageBitmap)

                imagePath = getRealPathFromUri(selectedImage)

            }else{
                Toast.makeText(this, "Não foi possivel selecinar a imagem", Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun uploadImage(confeiteiro: Confeiteiro){

        val file = File(imagePath)
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("foto", file.name, requestBody)
        val call = fotoService!!.uploadImage(body)

        call.enqueue(object : Callback<Foto> {

            override fun onResponse(call: Call<Foto>?, response: Response<Foto>?) {
                if(response!!.isSuccessful){
                    Toast.makeText(this@CadastroConfeiteiroActivity, "Imagem Enviada com Sucesso", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Foto>?, t: Throwable?) {
                Toast.makeText(this@CadastroConfeiteiroActivity, "ERRO!!! + ${t!!.message}", Toast.LENGTH_LONG).show()
                Log.d("ERRO IMAGEM", t.message)
            }



        })


    }

    fun getRealPathFromUri(uri:Uri):String{
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = contentResolver.query(uri, filePathColumn, null, null, null)!!
        cursor.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val result = cursor.getString(columnIndex)
        cursor.close()

        return result

    }






}
