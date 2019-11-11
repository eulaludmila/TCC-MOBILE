package tcc.sp.senai.br.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import tcc.sp.senai.br.showdebolos.R
import tcc.sp.senai.br.showdebolos.VisualizarProdutoActivity
import tcc.sp.senai.br.showdebolos.model.ConfeiteiroDTO
import tcc.sp.senai.br.showdebolos.model.Produto
import android.R.attr.name
import android.content.SharedPreferences
import android.support.v7.widget.LinearLayoutManager


class ProdutoCarrinhoAdapter (private val produtos:List<Produto>,
                              private val context: Context) : RecyclerView.Adapter<ProdutoCarrinhoAdapter.ViewHolder>() {

    var sharedPreferences: SharedPreferences? = null
    var cod:String = ""

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.adapter_produtos_carrinho, viewGroup, false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {

        sharedPreferences = context.getSharedPreferences("idValue",0)
        val produtosId = sharedPreferences!!.getStringSet("idProduto", mutableSetOf<String>())
        val list = ArrayList<String>(produtosId)

        return if (produtos != null) list.size else 0
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        sharedPreferences = context.getSharedPreferences("idValue",0)
        val produto = mutableListOf<Produto>()
        val produtosId = sharedPreferences!!.getStringSet("idProduto", mutableSetOf<String>())
        val list = ArrayList<String>(produtosId)

        for (i in 0 until list.size){

            val id = list[i]

            this.cod = id

            for (produtos in produtos){

                if (produtos.codProduto.toString() == cod){
                    produto.add(produtos)
                }

            }
        }


        holder.nomeProduto.text = produto[position].nomeProduto
        holder.precoProduto.text = "R$: " + produto[position].preco.toString()
        //        holder.localizacao.text = produtos[position].confeiteir
        var url = produto[position].foto
        Picasso.with(holder!!.fotoProduto.context).cancelRequest(holder!!.fotoProduto)
        Picasso.with(holder!!.fotoProduto.context).load("http://3.232.178.219$url").into(holder!!.fotoProduto)


    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var nomeProduto:TextView = itemView.findViewById(R.id.txt_nome_produto_carrinho)
        var localizacao:TextView = itemView.findViewById(R.id.txt_endereco_carrinho)
        var precoProduto:TextView = itemView.findViewById(R.id.txt_preco_carrinho)
        //        var avaliacao: RatingBar = itemView.findViewById(R.id.rt_avaliacao_produto_home)
        var fotoProduto: ImageView = itemView.findViewById(R.id.img_produto_carrinho)



    }
}