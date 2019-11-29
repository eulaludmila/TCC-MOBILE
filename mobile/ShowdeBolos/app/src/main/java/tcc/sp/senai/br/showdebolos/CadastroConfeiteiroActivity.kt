package tcc.sp.senai.br.showdebolos

import android.Manifest
import android.annotation.SuppressLint
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
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_cadastro_confeiteiro.*

import tcc.sp.senai.br.showdebolos.model.Celular

import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import tcc.sp.senai.br.showdebolos.services.FotosService
import tcc.sp.senai.br.showdebolos.tasks.VerificarEmailCpfTasks
import tcc.sp.senai.br.showdebolos.utils.Verificacao
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class CadastroConfeiteiroActivity : AppCompatActivity() {

    val COD_IMAGE = 101
    //forçando a varialvel a ser nula
//    var imageBitmap: Bitmap? = null
    var imagePath: String? = null
    var fotoService: FotosService? = null
    var permission: Array<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_confeiteiro)

        permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermissions(permission, 0)

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(permission, 0)

            return
        }

        fotoService = ApiConfig.getFotosService()

        txt_cpf_confeiteiro.addTextChangedListener(Mask.mask("###.###.###-##", txt_cpf_confeiteiro))
        txt_celular_confeiteiro.addTextChangedListener(Mask.mask("(##) #####-####", txt_celular_confeiteiro))
        txt_dt_nascimento_confeiteiro.addTextChangedListener(Mask.mask("##/##/####", txt_dt_nascimento_confeiteiro))

        val sexo = arrayOf("Selecione o Sexo","Masculino", "Feminino", "Outros", "Não Informar")
        val adapter = ArrayAdapter(this, R.layout.spinner_adapter ,sexo )


        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spn_sexo_confeiteiro.adapter = adapter


        img_add_foto_confeiteiro.setOnClickListener{
            abrirGaleria()
        }

        txt_email_confeiteiro.setOnFocusChangeListener { v, hasFocus ->

            if(hasFocus){

            }else{
                if(txt_email_confeiteiro.length() > 13){
                    carregando_confeiteiro.visibility = View.VISIBLE
                    val verificarEmailTasks = VerificarEmailCpfTasks(txt_email_confeiteiro.text.toString(),"confeiteiro","email",carregando_confeiteiro)

                    verificarEmailTasks.execute()

                    val retorno:String = verificarEmailTasks.get()

                    if(retorno == "1"){
                        val builder = AlertDialog.Builder(this@CadastroConfeiteiroActivity)
                        builder.setTitle("ERRO")
                        builder.setIcon(R.drawable.ic_erro)
                        builder.setMessage("Este e-mail já esta cadastrado")
                        builder.setPositiveButton("OK"){dialog, which ->  }
                        builder.show()
                        txt_email_confeiteiro.setText("")
                    }else{

                    }
                }
            }

        }

        txt_cpf_confeiteiro.setOnFocusChangeListener { v, hasFocus ->

            if(hasFocus){

            }else{
                if(txt_cpf_confeiteiro.length() === 14){

                    carregando_confeiteiro.visibility = View.VISIBLE
                    val verificarEmailCpfTasks = VerificarEmailCpfTasks(txt_cpf_confeiteiro.text.toString(),"confeiteiro","cpf",carregando_confeiteiro)

                    verificarEmailCpfTasks.execute()

                    val retorno:String = verificarEmailCpfTasks.get()

                    if(retorno == "1"){
                        val builder = AlertDialog.Builder(this@CadastroConfeiteiroActivity)
                        builder.setTitle("ERRO")
                        builder.setIcon(R.drawable.ic_erro)
                        builder.setMessage("Este cpf já esta cadastrado")
                        builder.setPositiveButton("OK"){dialog, which ->  }
                        builder.show()
                        txt_cpf_confeiteiro.setText("")
                    }

                }
            }

        }



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

                val sexoSelecionado = spnSexo.selectedItem.toString()


                val sexo = Verificacao.verificarSexo(sexoSelecionado)

                if (validar()) {
                    if (txtSenha.text.toString() == txtConfirmarSenha.text.toString() && txtSenha.length() > 8) {
                        val confeiteiro = Confeiteiro(0,
                                txtNome.text.toString(),
                                txtSobrenome.text.toString(),
                                txtCpf.text.toString(),
                                txtDtNascimento.text.toString(),
                                txtEmail.text.toString(),
                                txtSenha.text.toString(),
                                celular,
                                "teste.png",
                                sexo,
                                0.0)

                        if(imagePath == null){
                            Toast.makeText(this, "Selecione um arquivo de imagem", Toast.LENGTH_LONG);
                        }else{


                            Handler().postDelayed({
                                carregando_confeiteiro.setVisibility(View.VISIBLE)
                                val intent = Intent(this, CadastroEnderecoConfeiteiroActivity::class.java)
                                confeiteiro.foto = imagePath!!
                                intent.putExtra("confeiteiro", confeiteiro)
                                startActivity(intent)
                                finish()
                            },100)

                        }



                    } else {
                        Toast.makeText(this@CadastroConfeiteiroActivity, "As senhas não coincidem", Toast.LENGTH_LONG).show()
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
                val imageBitmap = BitmapFactory.decodeStream(inputStream)
                imagePath = getRealPathFromUri(selectedImage)

                //transformando o resultado em bitmap
                val rotadedBitmap = modifyOrientation(imageBitmap, imagePath!!)

                //exibir a imagem no aplicativo
                img_confeiteiro.setImageBitmap(rotadedBitmap)


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

        if(txt_nome_confeiteiro.text.toString().isEmpty()){
            layout_txt_nome_confeiteiro.isErrorEnabled = true
            layout_txt_nome_confeiteiro.error = resources.getText(0, "Digite o seu nome")
            validado = false
        }else if(txt_nome_confeiteiro.length() < 3 ){
            layout_txt_nome_confeiteiro.isErrorEnabled = true
            layout_txt_nome_confeiteiro.error = resources.getText(0, "Nome deve conter pelo menos 3 caracteres")
            validado = false
        }else{
            layout_txt_nome_confeiteiro.isErrorEnabled = false
        }

        if(txt_sobrenome_confeiteiro.text.toString().isEmpty()){
            layout_txt_sobrenome_confeiteiro.isErrorEnabled = true
            layout_txt_sobrenome_confeiteiro.error = resources.getText(0,"Digite o seu sobrenome")
            validado = false
        }else if(txt_sobrenome_confeiteiro.length() < 3 ){
            layout_txt_sobrenome_confeiteiro.isErrorEnabled = true
            layout_txt_sobrenome_confeiteiro.error = resources.getText(0, "Sobrenome deve conter pelo menos 3 caracteres")
            validado = false
        }else{
            layout_txt_sobrenome_confeiteiro.isErrorEnabled = false
        }

        if(txt_celular_confeiteiro.text.toString().isEmpty()){
            layout_txt_celular_confeiteiro.isErrorEnabled = true
            layout_txt_celular_confeiteiro.error = resources.getText(0, "Digite o seu celular")
            validado = false
        }else{
            layout_txt_celular_confeiteiro.isErrorEnabled = false
        }

        if(txt_cpf_confeiteiro.text.toString().isEmpty()){
            layout_txt_cpf_confeiteiro.isErrorEnabled = true
            layout_txt_cpf_confeiteiro.error = resources.getText(0, "Digite o seu cpf")
            validado = false
        }else if(txt_cpf_confeiteiro.length() < 14 ){
            layout_txt_cpf_confeiteiro.isErrorEnabled = true
            layout_txt_cpf_confeiteiro.error = resources.getText(0, "CPF deve estar no formato correto")
            validado = false
        }else{
            layout_txt_cpf_confeiteiro.isErrorEnabled = false
        }

        if(txt_dt_nascimento_confeiteiro.text.toString().isEmpty()){
            layout_txt_dt_nascimento_confeiteiro.isErrorEnabled = true
            layout_txt_dt_nascimento_confeiteiro.error = resources.getText(0, "Digite o seu nascimento")
            validado = false
        }else if(txt_dt_nascimento_confeiteiro.length() < 10 ){
            layout_txt_dt_nascimento_confeiteiro.isErrorEnabled = true
            layout_txt_dt_nascimento_confeiteiro.error = resources.getText(0, "Data deve estar no formato correto")
            validado = false
        }else{
            layout_txt_dt_nascimento_confeiteiro.isErrorEnabled = false
        }

        if(Verificacao.verificarSexo(spn_sexo_confeiteiro.selectedItem.toString()) == "SS"){
            val builder = AlertDialog.Builder(this@CadastroConfeiteiroActivity)
            builder.setTitle("ERRO")
            builder.setIcon(R.drawable.ic_erro)
            builder.setMessage("Selecione o sexo")
            builder.setPositiveButton("OK"){dialog, which ->  }
            builder.show()
            validado = false
        }else{

        }

        if(txt_email_confeiteiro.text.toString().isEmpty()){
            layout_txt_email_confeiteiro.isErrorEnabled = true
            layout_txt_email_confeiteiro.error = resources.getText(0, "Digite o seu e-mail")
            validado = false
        }else if(txt_email_confeiteiro.length() < 13 ){
            layout_txt_email_confeiteiro.isErrorEnabled = true
            layout_txt_email_confeiteiro.error = resources.getText(0, "E-mail deve estar no formato correto")
            validado = false
        }else{
            layout_txt_email_confeiteiro.isErrorEnabled = false
        }

        if(txt_senha_confeiteiro.text.toString().isEmpty()){
            layout_txt_senha_confeiteiro.isErrorEnabled = true
            layout_txt_senha_confeiteiro.error = resources.getText(0, "Digite a sua senha")
            validado = false
        }else if(txt_senha_confeiteiro.length() < 8 ){
            layout_txt_senha_confeiteiro.isErrorEnabled = true
            layout_txt_senha_confeiteiro.error = resources.getText(0, "Senha deve conter pelo menos 8 caracteres")
            validado = false
        }else{
            layout_txt_senha_confeiteiro.isErrorEnabled = false
        }

        if(txt_confirma_senha_confeiteiro.text.toString().isEmpty()){
            layout_txt_confirma_senha_confeiteiro.isErrorEnabled = true
            layout_txt_confirma_senha_confeiteiro.error = resources.getText(0, "")
            validado = false
        }else{
            layout_txt_confirma_senha_confeiteiro.isErrorEnabled = false
        }

        return validado
    }






}
