package tcc.sp.senai.br.showdebolos

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_cadastro_cliente.*
import tcc.sp.senai.br.showdebolos.model.Celular
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.showdebolos.utils.Verificacao
import java.util.*
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.widget.*
import tcc.sp.senai.br.showdebolos.tasks.CadastrarClienteTasks
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View.VISIBLE
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import tcc.sp.senai.br.showdebolos.services.FotosService
import tcc.sp.senai.br.showdebolos.tasks.VerificarEmailCpfTasks
import java.io.*


class CadastroClienteActivity : AppCompatActivity() {

    val COD_IMAGE = 101
    var inputStream: InputStream? = null
    //forçando a varialvel a ser nula
    var imageBitmap: Bitmap? = null
    var rotatedBitmap: Bitmap? = null
    var imagePath: String? = null
    var permission: Array<String>? = null
    var fotoService:FotosService? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @TargetApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cliente)

        fotoService = ApiConfig.getFotosService()

        permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermissions(permission, 0)

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(permission, 0)

            return
        }

        txt_cpf_cliente.addTextChangedListener(Mask.mask("###.###.###-##", txt_cpf_cliente))
        txt_celular_cliente.addTextChangedListener(Mask.mask("(##) #####-####", txt_celular_cliente))
        txt_dt_nascimento_cliente.addTextChangedListener(Mask.mask("##/##/####", txt_dt_nascimento_cliente))
        val sexo = arrayOf("Selecione o Sexo","Masculino", "Feminino", "Outros", "Não Informar")
        val adapter = ArrayAdapter(this, R.layout.spinner_adapter,sexo )


        img_add_foto_cliente.setOnClickListener {

            abrirGaleria()

        }

        txt_email_cliente.setOnFocusChangeListener { v, hasFocus ->

            if(hasFocus){

            }else{
                if(txt_email_cliente.length() > 13){
                    carregando.visibility = VISIBLE
                    val verificarEmailTasks = VerificarEmailCpfTasks(txt_email_cliente.text.toString(),"cliente","email",carregando)

                    verificarEmailTasks.execute()

                    val retorno:String = verificarEmailTasks.get()

                    if(retorno == "1"){
                        val builder = AlertDialog.Builder(this@CadastroClienteActivity)
                        builder.setTitle("ERRO")
                        builder.setIcon(R.drawable.ic_erro)
                        builder.setMessage("Este e-mail já esta cadastrado")
                        builder.setPositiveButton("OK"){dialog, which ->  }
                        builder.show()
                        txt_email_cliente.setText("")
                    }else{

                    }
                }
            }

        }

        txt_cpf_cliente.setOnFocusChangeListener { v, hasFocus ->

            if(hasFocus){

            }else{
                if(txt_cpf_cliente.length() == 14){

                    carregando.visibility = VISIBLE

                    val verificarEmailCpfTasks = VerificarEmailCpfTasks(txt_cpf_cliente.text.toString(),"cliente","cpf",carregando)

                    verificarEmailCpfTasks.execute()

                    val retorno:String = verificarEmailCpfTasks.get()

                    if(retorno == "1"){
                        val builder = AlertDialog.Builder(this@CadastroClienteActivity)
                        builder.setTitle("ERRO")
                        builder.setIcon(R.drawable.ic_erro)
                        builder.setMessage("Este cpf já esta cadastrado")
                        builder.setPositiveButton("OK"){dialog, which ->  }
                        builder.show()
                        txt_cpf_cliente.setText("")
                    }

                }
            }

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

                val sexoSelecionado = spnSexo.selectedItem.toString()


                val sexo = Verificacao.verificarSexo(sexoSelecionado)

//                Toast.makeText(this@CadastroClienteActivity, "sexo selecionado $sexoSelecionado", Toast.LENGTH_LONG).show()

                if(validar()){
                    if(txtSenha.text.toString() == txtConfirmarSenha.text.toString() && txtSenha.text.toString().length >= 8 ) {

                        val cliente = Cliente(0,
                                txtNome.text.toString(),
                                txtSobrenome.text.toString(),
                                txtCpf.text.toString(),
                                txtDtNascimento.text.toString(),
                                txtEmail.text.toString(),
                                txtSenha.text.toString(),
                                celular,
                                sexo,
                                "teste.png")

                        if(imagePath == null){
                            Toast.makeText(this, "Selecione um arquivo de imagem", Toast.LENGTH_LONG).show()
                        }else{

                            Handler().postDelayed({
                                val cadastrarCliente = CadastrarClienteTasks(cliente, celular)

                                cadastrarCliente.execute()

                                val retornoCliente = cadastrarCliente.get() as Cliente

                                uploadImage(retornoCliente,rotatedBitmap!!)

                                val intent = Intent(this, SucessoActivity::class.java)

                                startActivity(intent)

                                finish()

                            },100)

                        }

                    }else{
                        Toast.makeText(this@CadastroClienteActivity, "As senhas não coincidem", Toast.LENGTH_LONG).show()
                    }
                }

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
                imageBitmap = BitmapFactory.decodeStream(inputStream)

                imagePath = getRealPathFromUri(selectedImage)

                rotatedBitmap = modifyOrientation(imageBitmap!!,imagePath!!)
                //transformando o resultado em bitmap

                //exibir a imagem no aplicativo
                img_cliente.setImageBitmap(rotatedBitmap)


            }else{
                Toast.makeText(this, "Não foi possivel selecinar a imagem", Toast.LENGTH_SHORT).show()
            }

        }

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


    fun uploadImage(cliente: Cliente, bitmap: Bitmap){

        val file = File(imagePath)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30,stream)
        val image  = stream.toByteArray()
        val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), image)
        val codBody = RequestBody.create(MediaType.parse("text/plain"), cliente.codCliente.toString())
        val body = MultipartBody.Part.createFormData("foto", file.name, imageBody)

        val call = fotoService!!.uploadImageCliente(body, codBody)

        call.enqueue(object : Callback<Cliente>{

            override fun onResponse(call: Call<Cliente>?, response: Response<Cliente>?) {
                if(response!!.isSuccessful){
//                    Toast.makeText(this@CadastroClienteActivity, "Imagem Enviada com Sucesso", Toast.LENGTH_LONG).show()
            }
            }

            override fun onFailure(call: Call<Cliente>?, t: Throwable?) {
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


        @SuppressLint("ResourceAsColor")
    fun validar():Boolean{

        var validado = true

        if(txt_nome_cliente.text.toString().isEmpty()){
            layout_txt_nome_cliente.isErrorEnabled = true
            layout_txt_nome_cliente.error = resources.getText(0, "Digite o seu nome")
            validado = false
        }else if(txt_nome_cliente.length() < 3 ){
            layout_txt_nome_cliente.isErrorEnabled = true
            layout_txt_nome_cliente.error = resources.getText(0, "Nome deve conter pelo menos 3 caracteres")
            validado = false
        }else{
            layout_txt_nome_cliente.isErrorEnabled = false
        }

        if(txt_sobrenome_cliente.text.toString().isEmpty()){
            layout_txt_sobrenome_cliente.isErrorEnabled = true
            layout_txt_sobrenome_cliente.error = resources.getText(0,"Digite o seu sobrenome")
            validado = false
        }else if(txt_sobrenome_cliente.length() < 3 ){
            layout_txt_sobrenome_cliente.isErrorEnabled = true
            layout_txt_sobrenome_cliente.error = resources.getText(0, "Sobrenome deve conter pelo menos 3 caracteres")
            validado = false
        }else{
            layout_txt_sobrenome_cliente.isErrorEnabled = false
        }

        if(txt_celular_cliente.text.toString().isEmpty()){
            layout_txt_celular_cliente.isErrorEnabled = true
            layout_txt_celular_cliente.error = resources.getText(0, "Digite o seu celular")
            validado = false
        }else{
            layout_txt_celular_cliente.isErrorEnabled = false
        }

        if(txt_cpf_cliente.text.toString().isEmpty()){
            layout_txt_cpf_cliente.isErrorEnabled = true
            layout_txt_cpf_cliente.error = resources.getText(0, "Digite o seu cpf")
            validado = false
        }else if(txt_cpf_cliente.length() < 14 ){
            layout_txt_cpf_cliente.isErrorEnabled = true
            layout_txt_cpf_cliente.error = resources.getText(0, "CPF deve estar no formato correto")
            validado = false
        }else{
            layout_txt_cpf_cliente.isErrorEnabled = false
        }

        if(txt_dt_nascimento_cliente.text.toString().isEmpty()){
            layout_txt_dt_nascimento_cliente.isErrorEnabled = true
            layout_txt_dt_nascimento_cliente.error = resources.getText(0, "Digite o seu nascimento")
            validado = false
        }else if(txt_dt_nascimento_cliente.length() < 10 ){
            layout_txt_dt_nascimento_cliente.isErrorEnabled = true
            layout_txt_dt_nascimento_cliente.error = resources.getText(0, "Data deve estar no formato correto")
            validado = false
        }else{
            layout_txt_dt_nascimento_cliente.isErrorEnabled = false
        }

        if(Verificacao.verificarSexo(spn_sexo_cliente.selectedItem.toString()) == "SS"){
            val builder = AlertDialog.Builder(this@CadastroClienteActivity)
            builder.setTitle("ERRO")
            builder.setIcon(R.drawable.ic_erro)
            builder.setMessage("Selecione o sexo")
            builder.setPositiveButton("OK"){dialog, which ->  }
            builder.show()
            validado = false
        }else{
        }

        if(txt_email_cliente.text.toString().isEmpty()){
            layout_txt_email_cliente.isErrorEnabled = true
            layout_txt_email_cliente.error = resources.getText(0, "Digite o seu e-mail")
            validado = false
        }else if(txt_email_cliente.length() < 13 ){
            layout_txt_email_cliente.isErrorEnabled = true
            layout_txt_email_cliente.error = resources.getText(0, "E-mail deve estar no formato correto")
            validado = false
        }else{
            layout_txt_email_cliente.isErrorEnabled = false
        }

        if(txt_senha_cliente.text.toString().isEmpty()){
            layout_txt_senha_cliente.isErrorEnabled = true
            layout_txt_senha_cliente.error = resources.getText(0, "Digite a sua senha")
            validado = false
        }else if(txt_senha_cliente.length() < 8 ){
            layout_txt_senha_cliente.isErrorEnabled = true
            layout_txt_senha_cliente.error = resources.getText(0, "Senha deve conter pelo menos 8 caracteres")
            validado = false
        }else{
            layout_txt_senha_cliente.isErrorEnabled = false
        }

        if(txt_confirma_senha_cliente.text.toString().isEmpty()){
            layout_txt_confirma_senha_cliente.isErrorEnabled = true
            layout_txt_confirma_senha_cliente.error = resources.getText(0, "")
            validado = false
        }else{
            layout_txt_confirma_senha_cliente.isErrorEnabled = false
        }

        return validado
    }


}

