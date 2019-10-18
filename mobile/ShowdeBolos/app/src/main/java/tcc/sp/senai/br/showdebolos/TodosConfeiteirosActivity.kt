package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_todos_confeiteiros.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.adapter.ConfeiteiroHomeAdapter
import tcc.sp.senai.br.adapter.TodosConfeiteirosAdapter
import tcc.sp.senai.br.showdebolos.model.ConfeiteiroDTO
import tcc.sp.senai.br.showdebolos.services.ApiConfig

class TodosConfeiteirosActivity : AppCompatActivity() {

    val confeiteiro: ConfeiteiroDTO? = null
    var confeiteiros: List<ConfeiteiroDTO> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todos_confeiteiros)

        val recyclerViewTodosConfeiteiros: RecyclerView = findViewById(R.id.recyclerViewTodosConfeiteiros)
        recyclerViewTodosConfeiteiros.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        val callTodosConfeiteiros = ApiConfig.getConfeiteiroService().buscarTodosConfeiteiros()

        callTodosConfeiteiros.enqueue(object : Callback<List<ConfeiteiroDTO>> {

            override fun onResponse(call: Call<List<ConfeiteiroDTO>>, response: Response<List<ConfeiteiroDTO>>) {
//
                CarregarConfeiteiroHome(confeiteiros = response.body()!!)
                Log.i("Retrofit222", "fgfgfgf")
            }

            override fun onFailure(call: Call<List<ConfeiteiroDTO>>?, t: Throwable?) {
                Log.i("Retrofit", t?.message)
            }

        })


    }

    fun CarregarConfeiteiroHome(confeiteiros: List<ConfeiteiroDTO> ){

        this.confeiteiros = confeiteiros

        val todosConfeiteirosAdapter = TodosConfeiteirosAdapter(confeiteiros, this ,object : ConfeiteiroHomeAdapter.ConfeiteiroOnlickListener{
            override fun onClickConfeiteiro(view: View, index: Int) {
                val c = confeiteiros.get(index)
                AlertDialog.Builder(this@TodosConfeiteirosActivity)
                        .setTitle(c.nome)
                        .setMessage(c.sobrenome)
                        .show()
            }

        })

        recyclerViewTodosConfeiteiros.adapter = todosConfeiteirosAdapter

    }

}
