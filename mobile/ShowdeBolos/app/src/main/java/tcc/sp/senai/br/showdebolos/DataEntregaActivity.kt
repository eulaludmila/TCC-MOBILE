package tcc.sp.senai.br.showdebolos

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_data_entrega.*
import tcc.sp.senai.br.showdebolos.model.ProdutoDTO
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class DataEntregaActivity : AppCompatActivity() {

    var data = ""
    var total = 0.0
    var produtos:List<ProdutoDTO>? = null
    var codConfeiteiro = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_entrega)

        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, 1)
        calendarView.minDate = c.timeInMillis

        val formataData = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = calendarView.date

        data = formataData.format(calendar.time)


        total = intent.getSerializableExtra("total") as Double
        produtos = intent.getSerializableExtra("produtos") as List<ProdutoDTO>
        codConfeiteiro = intent.getSerializableExtra("confeiteiro") as Int

        calendarView.setOnDateChangeListener { calendarView, ano, mes, dia ->

            val mesCerto = mes+1

            data = "$ano-$mesCerto-$dia"

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


                val intent = Intent(this,HoraEntregaActivity::class.java)
                intent.putExtra("data",data)
                intent.putExtra("total",total)
                intent.putExtra("produtos",produtos as Serializable)
                intent.putExtra("confeiteiro", codConfeiteiro)
                startActivity(intent)

                finish()

            }

            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
