package tcc.sp.senai.br.showdebolos

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_agurdando_resposta_fragment.*
import retrofit2.Call
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.adapter.AguardandoRespostaAdapter
import tcc.sp.senai.br.showdebolos.adapter.StatusPedidoClienteAdapter
import tcc.sp.senai.br.showdebolos.model.Pedido
import tcc.sp.senai.br.showdebolos.services.ApiConfig

class PedidosFragment : Fragment() {

    var pedidos: List<Pedido>? = null
    var sharedPreferences: SharedPreferences? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_agurdando_resposta_fragment, container, false)
        sharedPreferences = this.activity!!.getSharedPreferences("idValue",0)
        val toolbar = view.findViewById(R.id.toolbar) as android.support.v7.widget.Toolbar

        val tipoPerfil = sharedPreferences!!.getString("tipoPerfil", "")

        if(activity is AppCompatActivity){

            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            toolbar.textAlignment = View.TEXT_ALIGNMENT_CENTER

            if(tipoPerfil == "cliente"){
                (activity as AppCompatActivity).title = "Status do Pedido"
            } else if(tipoPerfil == "confeiteiro"){
                (activity as AppCompatActivity).title = "Aguardando Resposta"
            }

        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewPedidosAguardando) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false)

       CarregarRecyclerView(view, tipoPerfil)
//
        toolbar.setNavigationOnClickListener {


            val fragmentManager: FragmentManager = activity!!.supportFragmentManager

            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.layout_fragment, FirstFragment()).commit() }

        return view
    }


    fun CarregarPedidos(pedidos: List<Pedido>, view: View, tipoPerfil:String ){

        if(tipoPerfil == "cliente"){
            val confeiteiroHomeAdapter = StatusPedidoClienteAdapter(pedidos, context!!)

            recyclerViewPedidosAguardando!!.adapter = confeiteiroHomeAdapter
        } else if(tipoPerfil == "confeiteiro"){
            val confeiteiroHomeAdapter = AguardandoRespostaAdapter(pedidos, context!!)

            recyclerViewPedidosAguardando!!.adapter = confeiteiroHomeAdapter
        }



    }

    fun CarregarRecyclerView(view: View, tipoPerfil: String){

        if(tipoPerfil == "confeiteiro"){
            val callPedido = ApiConfig.getItemPedidoEmAguarde()!!.buscarItemPedidoEmAguarde(sharedPreferences!!.getString("codUsuario", ""),sharedPreferences!!.getString("token", ""))


            callPedido.enqueue(object : retrofit2.Callback<List<Pedido>>{

                override fun onResponse(call: Call<List<Pedido>>, response: Response<List<Pedido>>) {

                    val listaPedidos = response.body()

                    if(listaPedidos != null){
                        CarregarPedidos(listaPedidos, view, tipoPerfil)
                    }


                }

                override fun onFailure(call: Call<List<Pedido>>?, t: Throwable?) {
                    Log.i("Retrofit33333", t?.message)
                }

            })
        } else if(tipoPerfil == "cliente"){
            val callPedido = ApiConfig.getPedido()!!.buscarPedidoCliente(sharedPreferences!!.getString("codUsuario", ""),sharedPreferences!!.getString("token", ""))

            callPedido.enqueue(object : retrofit2.Callback<List<Pedido>>{

                override fun onResponse(call: Call<List<Pedido>>, response: Response<List<Pedido>>) {

                    val listaPedidos = response.body()

                    if(listaPedidos != null){

                        CarregarPedidos(listaPedidos, view, tipoPerfil)
                        Log.d("pedido4444", listaPedidos.toString())

                    }


                }

                override fun onFailure(call: Call<List<Pedido>>?, t: Throwable?) {
                    Log.i("Retrofit33333", t?.message)
                }

            })
        }



    }

}
