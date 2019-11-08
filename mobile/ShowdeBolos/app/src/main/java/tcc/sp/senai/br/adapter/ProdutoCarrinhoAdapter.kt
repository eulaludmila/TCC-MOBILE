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

class ProdutoCarrinhoAdapter (private val produtos:List<Produto>,
                          private val context: Context) : RecyclerView.Adapter<ProdutoCarrinhoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.adapter_produtos_carrinho, viewGroup, false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {

        return if (produtos != null) produtos.size else 0
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        holder.nomeProduto.text = produtos[position].nomeProduto
        holder.precoProduto.text = "R$: " + produtos[position].preco.toString()
//        holder.localizacao.text = produtos[position].confeiteir
        var url = produtos[position].foto
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