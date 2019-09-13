package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_cadastro_cliente.*
import tcc.sp.senai.br.showdebolos.model.Celular
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.utils.Verificacao
import java.util.*
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.widget.*
import tcc.sp.senai.br.showdebolos.tasks.CadastrarClienteTasks
import java.io.ByteArrayOutputStream
import android.os.Build
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.util.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.model.Foto
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import tcc.sp.senai.br.showdebolos.services.FotosService
import tcc.sp.senai.br.showdebolos.tasks.CadastrarFotoClienteTasks
import java.io.File
import java.net.URI
import kotlin.math.log


class CadastroClienteActivity : AppCompatActivity() {

    val COD_IMAGE = 101
    //forçando a varialvel a ser nula
    var imageBitmap: Bitmap? = null
    var imagePath: String? = null
    var fotoService:FotosService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cliente)

        fotoService = ApiConfig.getFotosService()

        txt_cpf_cliente.addTextChangedListener(Mask.mask("###.###.###-##", txt_cpf_cliente))
        txt_celular_cliente.addTextChangedListener(Mask.mask("(##) #####-####", txt_celular_cliente))
        txt_dt_nascimento_cliente.addTextChangedListener(Mask.mask("##/##/####", txt_dt_nascimento_cliente))
        val sexo = arrayOf("Selecione o Sexo","Masculino", "Feminino", "Outros")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item
                ,sexo )

        img_add_foto_cliente.setOnClickListener {

            abrirGaleria()

        }

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spn_sexo_cliente.adapter = adapter



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

                val txtNome = findViewById<TextView>(R.id.txt_nome_cliente)
                val txtSobrenome = findViewById<TextView>(R.id.txt_sobrenome_cliente)
                val txtCpf = findViewById<TextView>(R.id.txt_cpf_cliente)
                val txtDtNascimento = findViewById<TextView>(R.id.txt_dt_nascimento_cliente)
                val txtEmail = findViewById<TextView>(R.id.txt_email_cliente)
                val txtSenha = findViewById<TextView>(R.id.txt_senha_cliente)
                val spnSexo = findViewById<Spinner>(R.id.spn_sexo_cliente)
                val txtConfirmarSenha = findViewById<TextView>(R.id.txt_confirma_senha_cliente)
                val imgCliente = findViewById<ImageView>(R.id.img_cliente)

                val txtCelular = findViewById<TextView>(R.id.txt_celular_cliente)

                val celular = Celular(0, txtCelular.text.toString())

                val bitmap = (imgCliente.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val imgClienteByte = baos.toByteArray()
                val imgArray = Base64.getEncoder().encodeToString(imgClienteByte)

                val cliente = Cliente(0,
                        txtNome.text.toString(),
                        txtSobrenome.text.toString(),
                        txtCpf.text.toString(),
                        "04/06/2001",
                        txtEmail.text.toString(),
                        txtSenha.text.toString(),
                        celular,
                        "F",
                        "teste.png")

//                val sexo = Verificacao.verificarSexo(sexoSelecionado.toString())
                val senha = Verificacao.verificarSenha(txtSenha.text.toString(), txtConfirmarSenha.text.toString())



                val cadastrarCliente = CadastrarClienteTasks(cliente, celular)

                cadastrarCliente.execute()

                val retornoCliente = cadastrarCliente.get() as Cliente

//                val cadastrarFoto = CadastrarFotoClienteTasks(retornoCliente.codCliente, imgArray)
//
//                cadastrarFoto.execute()
                uploadImage(retornoCliente)

                Toast.makeText(this, cadastrarCliente.cliente.nome, Toast.LENGTH_SHORT).show()

                //Toast.makeText(this, .toString(), Toast.LENGTH_LONG).show()
                finish()
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

    fun uploadImage(cliente: Cliente){

        val file = File(imagePath)
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("foto", file.name, requestBody)
        val call = fotoService!!.uploadImage(body)

        call.enqueue(object : Callback<Foto>{

            override fun onResponse(call: Call<Foto>?, response: Response<Foto>?) {
                if(response!!.isSuccessful){
                    Toast.makeText(this@CadastroClienteActivity, "Imagem Enviada com Sucesso", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Foto>?, t: Throwable?) {
                Toast.makeText(this@CadastroClienteActivity, "ERRO!!! + ${t!!.message}", Toast.LENGTH_LONG).show()
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