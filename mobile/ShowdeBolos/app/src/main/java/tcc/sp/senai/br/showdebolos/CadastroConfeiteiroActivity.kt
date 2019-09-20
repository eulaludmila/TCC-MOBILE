package tcc.sp.senai.br.showdebolos

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import android.widget.*
import kotlinx.android.synthetic.main.activity_cadastro_confeiteiro.*
import kotlinx.android.synthetic.main.activity_cadastro_confeiteiro.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.model.Celular

import tcc.sp.senai.br.showdebolos.model.Confeiteiro
import tcc.sp.senai.br.showdebolos.model.EnderecoConfeiteiro
import tcc.sp.senai.br.showdebolos.model.Foto
import tcc.sp.senai.br.showdebolos.services.ApiConfig
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
                                sexo)


                        val cadastroConfeiteiro = CadastrarConfeiteiroTasks(confeiteiro)
                        cadastroConfeiteiro.execute()

                        val retornoConfeiteiro = cadastroConfeiteiro.get() as Confeiteiro

                        uploadImage(retornoConfeiteiro)

                        val intent = Intent(this, CadastroEnderecoConfeiteiroActivity::class.java)
                        intent.putExtra("confeiteiro", retornoConfeiteiro)
                        startActivity(intent)


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


                //transformando o resultado em bitmap
                imageBitmap = BitmapFactory.decodeStream(inputStream)

                //exibir a imagem no aplicativo
                img_confeiteiro.setImageBitmap(imageBitmap)

                imagePath = getRealPathFromUri(selectedImage)

            }else{
                Toast.makeText(this, "Não foi possivel selecinar a imagem", Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun uploadImage(confeiteiro: Confeiteiro){

        val file = File(imagePath)

        val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val codBody = RequestBody.create(MediaType.parse("text/plain"), confeiteiro.codConfeiteiro.toString())
        val body = MultipartBody.Part.createFormData("foto", file.name, imageBody)



        val call = fotoService!!.uploadImageConfeiteiro(body, codBody)

        call.enqueue(object : Callback<Confeiteiro>{

            override fun onResponse(call: Call<Confeiteiro>?, response: Response<Confeiteiro>?) {
                if(response!!.isSuccessful){
                    Toast.makeText(this@CadastroConfeiteiroActivity, "Imagem Enviada com Sucesso", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Confeiteiro>?, t: Throwable?) {
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
