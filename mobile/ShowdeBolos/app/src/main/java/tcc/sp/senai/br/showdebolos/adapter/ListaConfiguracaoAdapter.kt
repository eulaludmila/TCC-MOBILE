package tcc.sp.senai.br.showdebolos.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import tcc.sp.senai.br.showdebolos.CircularReveal
import tcc.sp.senai.br.showdebolos.R
import tcc.sp.senai.br.showdebolos.model.ListaConfiguracao

class ListaConfiguracaoAdapter (private val itens :List<ListaConfiguracao>,
                               private val context: Context) : RecyclerView.Adapter<ListaConfiguracaoAdapter.ViewHolder>() {


    var mPreferences: SharedPreferences? = null
    var mEditor:SharedPreferences.Editor? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int):ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.adapter_itens_configuracao_fragment, viewGroup, false)

        return ViewHolder(view)

    }

    override fun getItemCount()= if(itens != null) itens.size else 0


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.titulo.text = itens[position].titulo
        holder.subtitulo.text = itens[position].subtitulo
        val image = BitmapFactory.decodeResource(context.resources, itens[position].imagem)
        holder.imagem.setImageBitmap(image)

        holder.adapter.setOnClickListener {
            when(itens[position].titulo) {
                "Produtos" -> {}
                "Endereço" -> "lala"
                "Histórico" -> "lala"
                "Pedidos" -> "lala"
                "Sair" -> {
                    mPreferences = context!!.getSharedPreferences("idValue", 0)
                    mEditor = mPreferences!!.edit()
                    mEditor!!.clear()
                    mEditor!!.apply()
                    val intent = Intent(context, CircularReveal::class.java)
                    context!!.startActivity(intent)
                    (context!! as Activity).finish()

                }

                else -> Log.d("MENSAGENS", "Erroooor")
            }
        }




    }



    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var imagem: ImageView = itemView.findViewById(R.id.img_icone_config)
        var titulo:TextView = itemView.findViewById(R.id.txt_titulo_config)
        var subtitulo:TextView = itemView.findViewById(R.id.txt_subtitulo_config)
        var adapter: LinearLayout = itemView.findViewById(R.id.adapter)
    }


}