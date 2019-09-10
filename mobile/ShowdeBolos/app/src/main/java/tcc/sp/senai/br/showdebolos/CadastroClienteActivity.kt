package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_cadastro_cliente.*
import tcc.sp.senai.br.showdebolos.model.Celular
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.util.Verificacao
import java.util.*
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.*
import tcc.sp.senai.br.showdebolos.tasks.CadastrarClienteTasks
import java.io.ByteArrayOutputStream
import android.os.Build
import android.support.annotation.RequiresApi
import tcc.sp.senai.br.showdebolos.tasks.CadastrarFotoClienteTasks


class CadastroClienteActivity : AppCompatActivity() {

    val COD_IMAGE = 101
    //forçando a varialvel a ser nula
    var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cliente)

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

                val cadastrarFoto = CadastrarFotoClienteTasks(retornoCliente.codCliente, imgArray)

                cadastrarFoto.execute()

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


//    fun cadastrarCelular() {
//
//
//
//        val url = URL("http://10.107.144.10:8080/celular")
//
//        doAsync {
//
//            val jsCelular = JSONStringer()
//
//            jsCelular.`object`()
//            jsCelular.key("celular").value(celular.celular)
//            jsCelular.endObject()
//
//            val conexao = url.openConnection() as HttpURLConnection
//
//            conexao.setRequestProperty("Content-Type", "application/json")
//            conexao.setRequestProperty("Accept", "application/json")
//            conexao.requestMethod = "POST"
//
//            conexao.doInput = true
//
//            val output = PrintStream(conexao.outputStream)
//            output.print(jsCelular)
//
//            conexao.connect()
//
//            val scanner = Scanner(conexao.inputStream)
//            val resposta = scanner.nextLine()
//
//            val codCel = JSONObject(resposta).getInt("codCelular")
//            val cel = JSONObject(resposta).getString("celular")
//
//            val retornoCelular = Celular(codCel, cel)
//
//            uiThread {
//                cadastrarCliente(retornoCelular)
//            }
//
//        }
//
//
//    }

    fun abrirGaleria(){
        //definindo a ação de conteudo
        val intent = Intent(Intent.ACTION_GET_CONTENT)

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
                val inputStream = contentResolver.openInputStream(data.data)

                //transformando o resultado em bitmap
                imageBitmap = BitmapFactory.decodeStream(inputStream)

                //exibir a imagem no aplicativo
                img_cliente.setImageBitmap(imageBitmap)


            }
        }

    }

    fun cadastrarCliente(celular: Celular) {



//        Toast.makeText(this, "NOME: " + txtNome.text.toString(), Toast.LENGTH_LONG).show()


//        ArrayAdapter.createFromResource(
//                this,
//                R.array.array_sexo,
//                android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spnSexo.adapter = adapter
//        }
//
//        val sexoSelecionado = spnSexo.selectedItem
//
//

//        if (senha){
//
//
//
//            val url = URL("http://10.107.144.10:8080/cliente")
//
//            doAsync {
//
//
//
//
//                uiThread {
//                    alert("Cadastrado com sucesso!")
//                }
//
//            }
//
//        } else {
//            Toast.makeText(this,"Senhas não coincidem", Toast.LENGTH_LONG).show()
//        }




    }
}