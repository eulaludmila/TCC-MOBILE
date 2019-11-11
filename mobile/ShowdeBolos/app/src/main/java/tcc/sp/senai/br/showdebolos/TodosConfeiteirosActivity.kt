package tcc.sp.senai.br.showdebolos

import android.content.SharedPreferences
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
import tcc.sp.senai.br.showdebolos.adapter.ConfeiteiroHomeAdapter
import tcc.sp.senai.br.showdebolos.adapter.TodosConfeiteirosAdapter
import tcc.sp.senai.br.showdebolos.model.EnderecoConfeiteiro
import tcc.sp.senai.br.showdebolos.services.ApiConfig

class TodosConfeiteirosActivity : AppCompatActivity() {

    val confeiteiro: EnderecoConfeiteiro? = null
    var confeiteiros: List<EnderecoConfeiteiro> = ArrayList()
    var sharedPreferences:SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todos_confeiteiros)

        sharedPreferences = getSharedPreferences("idValue",0)

        val recyclerViewTodosConfeiteiros: RecyclerView = findViewById(R.id.recyclerViewTodosConfeiteiros)
        recyclerViewTodosConfeiteiros.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        val callTodosConfeiteiros = ApiConfig.getConfeiteiroService().buscarTodosConfeiteiros(sharedPreferences!!.getString("token", ""))

        callTodosConfeiteiros.enqueue(object : Callback<List<EnderecoConfeiteiro>> {

            override fun onResponse(call: Call<List<EnderecoConfeiteiro>>, response: Response<List<EnderecoConfeiteiro>>) {
//
                CarregarConfeiteiroHome(confeiteiros = response.body()!!)
                Log.i("Retrofit222", "fgfgfgf")
            }

            override fun onFailure(call: Call<List<EnderecoConfeiteiro>>?, t: Throwable?) {
                Log.i("Retrofit", t?.message)
            }

        })


    }

    fun CarregarConfeiteiroHome(confeiteiros: List<EnderecoConfeiteiro> ){

        this.confeiteiros = confeiteiros

        val todosConfeiteirosAdapter = TodosConfeiteirosAdapter(confeiteiros, this ,object : ConfeiteiroHomeAdapter.ConfeiteiroOnlickListener{
            override fun onClickConfeiteiro(view: View, index: Int) {
                val c = confeiteiros.get(index)
                AlertDialog.Builder(this@TodosConfeiteirosActivity)
                        .setTitle(c.confeiteiro.nome)
                        .setMessage(c.confeiteiro.sobrenome)
                        .show()
            }

        })

        recyclerViewTodosConfeiteiros.adapter = todosConfeiteirosAdapter

    }

}
