package tcc.sp.senai.br.showdebolos

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_hora_entrega.*
import tcc.sp.senai.br.showdebolos.model.Celular
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.showdebolos.model.ProdutoDTO
import tcc.sp.senai.br.showdebolos.tasks.CadastrarClienteTasks
import tcc.sp.senai.br.showdebolos.utils.Verificacao
import java.io.ByteArrayOutputStream
import java.io.Serializable
import java.util.*

class HoraEntregaActivity : AppCompatActivity() {

    var total = 0.0
    var produtos:List<ProdutoDTO>? = null
    var codConfeiteiro = 0
    var data = ""
    var hora = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hora_entrega)

        timePicker.setIs24HourView(true)

        try {
            val field = TextInputLayout::class.java.getDeclaredField("defaultStrokeColor")
            field.isAccessible = true
            field.set(layout_txt_observacao, ContextCompat.getColor(layout_txt_observacao.context, R.color.preto))
        } catch (e: NoSuchFieldException) {
            Log.w("TAG", "Failed to change box color, item might look wrong")
        } catch (e: IllegalAccessException) {
            Log.w("TAG", "Failed to change box color, item might look wrong")
        }
        total = intent.getSerializableExtra("total") as Double
        produtos = intent.getSerializableExtra("produtos") as List<ProdutoDTO>
        codConfeiteiro = intent.getSerializableExtra("confeiteiro") as Int
        data = intent.getStringExtra("data") as String

        timePicker.setOnTimeChangedListener { timePicker, hora, minuto ->



            this.hora = "${hora}:${minuto}"



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


                val intent = Intent(this, PagamentoActivity::class.java)
                intent.putExtra("hora",hora)
                intent.putExtra("data",data)
                intent.putExtra("total",total)
                intent.putExtra("produtos",produtos as Serializable)
                intent.putExtra("confeiteiro", codConfeiteiro)
                intent.putExtra("observacao", txt_observacao.text.toString())

                startActivity(intent)
                finish()


            }

            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

