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
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_fragment.*
import kotlinx.android.synthetic.main.activity_pagamento_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.adapter.PagamentoPedidosAdapter
import tcc.sp.senai.br.showdebolos.model.Pedido
import tcc.sp.senai.br.showdebolos.services.ApiConfig

class PedidosParaSeremPagosFragment : Fragment() {

    var pedidos: List<Pedido> = ArrayList()
    var sharedPreferences: SharedPreferences? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_pagamento_fragment, container, false)
        sharedPreferences = this.activity!!.getSharedPreferences("idValue",0)
        val toolbar = view.findViewById(R.id.toolbar) as android.support.v7.widget.Toolbar

        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            toolbar.textAlignment = View.TEXT_ALIGNMENT_CENTER
            (activity as AppCompatActivity).title = "Pagamento"
        }

        val recyclerViewPagamento: RecyclerView = view.findViewById(R.id.recyclerViewPagamento) as RecyclerView
        recyclerViewPagamento.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false)

        CarregarRecyclerView()


        toolbar.setNavigationOnClickListener {


            val fragmentManager: FragmentManager = activity!!.supportFragmentManager

            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.layout_fragment, FirstFragment()).commit() }

        return view
    }


    fun CarregarPedidos(pedidos: List<Pedido> ){

        this.pedidos = pedidos

        val confeiteiroHomeAdapter = PagamentoPedidosAdapter(pedidos, context!!)

        recyclerViewPagamento.adapter = confeiteiroHomeAdapter

    }

    fun CarregarRecyclerView(){
        val callPedido = ApiConfig.getPedidosParaPagarService()!!.buscarPedidosParaSeremPagos("89",sharedPreferences!!.getString("token", ""))


        callPedido.enqueue(object : Callback<List<Pedido>>{

            override fun onResponse(call: Call<List<Pedido>>, response: Response<List<Pedido>>) {

                CarregarPedidos(pedidos = response.body()!!)
                Log.i("Retrofit222", "fgfgfgf")
            }

            override fun onFailure(call: Call<List<Pedido>>?, t: Throwable?) {
                Log.i("Retrofit33333", t?.message)
            }

        })
    }

}
