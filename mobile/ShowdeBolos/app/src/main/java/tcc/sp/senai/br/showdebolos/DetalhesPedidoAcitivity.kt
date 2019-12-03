package tcc.sp.senai.br.showdebolos

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.adapter.DetalhesPedidoAdapter
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.showdebolos.model.EnderecoCliente
import tcc.sp.senai.br.showdebolos.model.ItemPedido
import tcc.sp.senai.br.showdebolos.services.ApiConfig

class DetalhesPedidoAcitivity : AppCompatActivity() {

    var mPreferences: SharedPreferences? = null
    var token:String? = null
    var idPerfil:String? = null
    var pedidos:JsonArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_pedido_acitivity)

        mPreferences = this!!.getSharedPreferences("idValue", 0)
        token = mPreferences!!.getString("token","")

        idPerfil  = mPreferences!!.getString("codCliente","")


        val codPedido = intent.getIntExtra("codPedido", 0)

        toast(codPedido.toString())

//        val call = ApiConfig.getItensPedido().buscarItensPedido(codPedido, this.token!!)
//
//        call.enqueue(object : retrofit2.Callback<JsonArray>{
//            override fun onFailure(call: Call<JsonArray>?, t: Throwable?) {
//
//
//                Log.d("ERRO_ENDERECO_CLIENTE", t!!.message)
//            }
//
//            override fun onResponse(call: Call<JsonArray>?, response: Response<JsonArray>?) {
//                pedidos = response!!.body()!!
//
//                var arrayPedido:List<ItemPedido> = ArrayList()
//
//
//
//                //val detalhesPedidoAdapter = DetalhesPedidoAdapter(pedidos, this@DetalhesPedidoAcitivity)
//
//
//                Log.d("ENDERECO_CLIENTE", pedidos!!.toString())
//            }
//        })



    }
}
